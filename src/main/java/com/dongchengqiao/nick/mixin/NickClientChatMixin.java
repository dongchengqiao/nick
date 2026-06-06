package com.dongchengqiao.nick.mixin;

import com.dongchengqiao.nick.NickClientConfig;
import com.dongchengqiao.nick.NickClientConfig.DisplayLocation;
import com.dongchengqiao.nick.NickClientConfig.DisplayMode;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundPlayerChatPacket;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;

@Mixin(ClientPacketListener.class)
public class NickClientChatMixin {
	private UUID nickClientChatSenderUUID;

	@Inject(method = "handlePlayerChat", at = @At("HEAD"))
	private void captureChatSender(ClientboundPlayerChatPacket packet, CallbackInfo ci) {
		DisplayMode mode = NickClientConfig.getDisplayMode(DisplayLocation.CHAT);
		if (mode == DisplayMode.DEFAULT || mode == DisplayMode.NICK_ONLY) {
			nickClientChatSenderUUID = null;
		} else {
			nickClientChatSenderUUID = packet.sender();
		}
	}

	@ModifyArg(
		method = "handlePlayerChat",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/multiplayer/chat/ChatListener;handlePlayerChatMessage(Lnet/minecraft/network/chat/PlayerChatMessage;Lcom/mojang/authlib/GameProfile;Lnet/minecraft/network/chat/ChatType$Bound;)V"
		),
		index = 2
	)
	private ChatType.Bound modifyChatBound(ChatType.Bound bound) {
		UUID uuid = nickClientChatSenderUUID;
		if (uuid == null) return bound;

		PlayerInfo info = ((ClientPacketListener)(Object)this).getPlayerInfo(uuid);
		if (info == null) return bound;

		String originalName = info.getProfile().name();
		DisplayMode mode = NickClientConfig.getDisplayMode(DisplayLocation.CHAT);

		Component displayName;
		switch (mode) {
			case HIDE:
				displayName = Component.literal(originalName);
				break;
			case NICK_AND_ORIGINAL:
				String nickText = bound.name().getString();
				displayName = Component.literal("[" + nickText + "]" + originalName);
				break;
			default:
				return bound;
		}

		return new ChatType.Bound(bound.chatType(), displayName, bound.targetName());
	}

	@Inject(method = "handlePlayerChat", at = @At("RETURN"))
	private void cleanupChat(CallbackInfo ci) {
		nickClientChatSenderUUID = null;
	}
}
