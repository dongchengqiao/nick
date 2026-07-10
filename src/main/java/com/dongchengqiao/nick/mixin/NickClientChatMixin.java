package com.dongchengqiao.nick.mixin;

import com.dongchengqiao.nick.NickClientConfig;
import com.dongchengqiao.nick.NickClientConfig.DisplayLocation;
import com.dongchengqiao.nick.NickClientConfig.DisplayMode;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.protocol.game.ClientboundPlayerChatPacket;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.scores.Scoreboard;
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
		nickClientChatSenderUUID = (mode == DisplayMode.DEFAULT) ? null : packet.sender();
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
			case NICK_ONLY: {
				String nickname = findNicknameByPlayerInfo(info);
				if (nickname == null) return bound;
				displayName = Component.literal(nickname);
				break;
			}
			case NICK_AND_ORIGINAL: {
				String nickname = findNicknameByPlayerInfo(info);
				if (nickname == null) return bound;
				displayName = Component.literal("[" + nickname + "]" + originalName);
				break;
			}
			default:
				return bound;
		}

		PlayerTeam team = getTeamForPlayer(originalName);
		return new ChatType.Bound(bound.chatType(), formatWithTeam(team, displayName), bound.targetName());
	}

	@Inject(method = "handlePlayerChat", at = @At("RETURN"))
	private void cleanupChat(CallbackInfo ci) {
		nickClientChatSenderUUID = null;
	}

	private static Component formatWithTeam(PlayerTeam team, Component name) {
		if (team == null) return name;
		MutableComponent result = Component.empty();
		Component prefix = team.getPlayerPrefix();
		if (prefix != null) {
			result.append(prefix);
		}
		if (team.getColor() != ChatFormatting.RESET) {
			result.append(name.copy().withStyle(team.getColor()));
		} else {
			result.append(name);
		}
		Component suffix = team.getPlayerSuffix();
		if (suffix != null) {
			result.append(suffix);
		}
		return result;
	}

	private static String findNicknameByPlayerInfo(PlayerInfo info) {
		Minecraft mc = Minecraft.getInstance();
		if (mc.level == null) return null;
		for (Player player : mc.level.players()) {
			if (player.getUUID().equals(info.getProfile().id())) {
				Component customName = player.getCustomName();
				return customName != null ? customName.getString() : null;
			}
		}
		return null;
	}

	private static PlayerTeam getTeamForPlayer(String playerName) {
		Minecraft mc = Minecraft.getInstance();
		if (mc.level == null) return null;
		Scoreboard scoreboard = mc.level.getScoreboard();
		return scoreboard.getPlayersTeam(playerName);
	}
}
