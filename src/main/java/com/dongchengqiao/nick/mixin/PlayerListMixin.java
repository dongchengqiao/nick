package com.dongchengqiao.nick.mixin;

import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.OutgoingChatMessage;
import net.minecraft.network.chat.PlayerChatMessage;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import net.minecraft.world.scores.PlayerTeam;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.function.Predicate;

@Mixin(PlayerList.class)
public class PlayerListMixin {
	@Shadow
	private List<ServerPlayer> players;

	private static final ThreadLocal<ServerPlayer> CURRENT_SENDER = new ThreadLocal<>();

	@Inject(method = "getPlayerByName", at = @At("TAIL"), cancellable = true)
	private void onGetPlayerByName(String name, CallbackInfoReturnable<ServerPlayer> cir) {
		if (cir.getReturnValue() == null) {
			for (ServerPlayer player : players) {
				Component customName = player.getCustomName();
				if (customName != null && customName.getString().equals(name)) {
					cir.setReturnValue(player);
					return;
				}
			}
		}
	}

	@Inject(method = "getPlayerNamesArray", at = @At("HEAD"), cancellable = true)
	private void onGetPlayerNamesArray(CallbackInfoReturnable<String[]> cir) {
		String[] result = new String[players.size()];
		for (int i = 0; i < players.size(); i++) {
			ServerPlayer player = players.get(i);
			Component customName = player.getCustomName();
			if (customName != null) {
				result[i] = customName.getString();
			} else {
				result[i] = player.getScoreboardName();
			}
		}
		cir.setReturnValue(result);
	}

	@Inject(
		method = "broadcastChatMessage(Lnet/minecraft/network/chat/PlayerChatMessage;Ljava/util/function/Predicate;Lnet/minecraft/server/level/ServerPlayer;Lnet/minecraft/network/chat/ChatType$Bound;)V",
		at = @At("HEAD")
	)
	private void captureSender(PlayerChatMessage message, Predicate<ServerPlayer> predicate, ServerPlayer sender, ChatType.Bound bound, CallbackInfo ci) {
		CURRENT_SENDER.set(sender);
	}

	@ModifyArg(
		method = "broadcastChatMessage(Lnet/minecraft/network/chat/PlayerChatMessage;Ljava/util/function/Predicate;Lnet/minecraft/server/level/ServerPlayer;Lnet/minecraft/network/chat/ChatType$Bound;)V",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/server/level/ServerPlayer;sendChatMessage(Lnet/minecraft/network/chat/OutgoingChatMessage;ZLnet/minecraft/network/chat/ChatType$Bound;)V"
		),
		index = 2
	)
	private ChatType.Bound modifyBoundForPlayer(ChatType.Bound bound) {
		ServerPlayer sender = CURRENT_SENDER.get();
		if (sender != null) {
			Component customName = sender.getCustomName();
			if (customName != null) {
				Component formatted = PlayerTeam.formatNameForTeam(sender.getTeam(), customName);
				return new ChatType.Bound(bound.chatType(), formatted, bound.targetName());
			}
		}
		return bound;
	}

	@Inject(
		method = "broadcastChatMessage(Lnet/minecraft/network/chat/PlayerChatMessage;Ljava/util/function/Predicate;Lnet/minecraft/server/level/ServerPlayer;Lnet/minecraft/network/chat/ChatType$Bound;)V",
		at = @At("RETURN")
	)
	private void cleanupSender(CallbackInfo ci) {
		CURRENT_SENDER.remove();
	}
}
