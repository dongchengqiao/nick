package com.dongchengqiao.nick.client;

import com.dongchengqiao.nick.mixin.PlayerInfoAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;

import java.util.Locale;
import java.util.UUID;

public final class NickClientUtil {
	private NickClientUtil() {
	}

	public static String getNickname(Player player) {
		Component customName = player.getCustomName();
		if (customName != null && !customName.getString().isEmpty()) {
			return customName.getString();
		}
		return getNicknameByUuid(player.getUUID());
	}

	public static String getNicknameByUuid(UUID uuid) {
		Minecraft minecraft = Minecraft.getInstance();
		if (minecraft.level != null) {
			for (Player player : minecraft.level.players()) {
				if (player.getUUID().equals(uuid)) {
					Component customName = player.getCustomName();
					if (customName != null && !customName.getString().isEmpty()) {
						return customName.getString();
					}
					break;
				}
			}
		}
		if (minecraft.getConnection() != null) {
			PlayerInfo info = minecraft.getConnection().getPlayerInfo(uuid);
			if (info != null) {
				Component displayName = ((PlayerInfoAccessor) info).nick$getRawTabListDisplayName();
				if (displayName != null) {
					String text = displayName.getString();
					String originalName = info.getProfile().name();
					boolean isOriginal = text.equalsIgnoreCase(originalName)
						|| text.toLowerCase(Locale.ROOT).contains(originalName.toLowerCase(Locale.ROOT));
					if (!text.isEmpty() && !isOriginal) {
						return text;
					}
				}
			}
		}
		return null;
	}
}
