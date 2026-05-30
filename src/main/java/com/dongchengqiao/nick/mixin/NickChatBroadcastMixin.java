package com.dongchengqiao.nick.mixin;

import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.OutgoingChatMessage;
import net.minecraft.network.chat.PlayerChatMessage;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Predicate;

@Mixin(PlayerList.class)
public class NickChatBroadcastMixin {
	private static final ThreadLocal<ServerPlayer> CURRENT_SENDER = new ThreadLocal<>();

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
				return new ChatType.Bound(bound.chatType(), customName, bound.targetName());
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
