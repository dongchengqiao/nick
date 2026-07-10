package com.dongchengqiao.nick.mixin;

import com.dongchengqiao.nick.NickClientConfig;
import com.dongchengqiao.nick.NickClientConfig.DisplayLocation;
import com.dongchengqiao.nick.NickClientConfig.DisplayMode;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.scores.Scoreboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.UUID;

@Mixin(PlayerInfo.class)
public class NickClientTabListMixin {
	@Inject(method = "getTabListDisplayName", at = @At("HEAD"), cancellable = true)
	private void onGetTabListDisplayName(CallbackInfoReturnable<Component> cir) {
		DisplayMode mode = NickClientConfig.getDisplayMode(DisplayLocation.TAB_LIST);
		if (mode == DisplayMode.DEFAULT) {
			return;
		}

		String originalName = ((PlayerInfo)(Object)this).getProfile().name();
		Component display;

		switch (mode) {
			case HIDE:
				display = Component.literal(originalName);
				break;
			case NICK_ONLY: {
				UUID uuid = ((PlayerInfo)(Object)this).getProfile().id();
				String nickname = findNicknameByUUID(uuid);
				if (nickname == null) return;
				display = Component.literal(nickname);
				break;
			}
			case NICK_AND_ORIGINAL: {
				UUID uuid = ((PlayerInfo)(Object)this).getProfile().id();
				String nickname = findNicknameByUUID(uuid);
				if (nickname == null) return;
				display = Component.literal("[" + nickname + "]" + originalName);
				break;
			}
			default:
				return;
		}

		PlayerTeam team = getTeamForPlayer(originalName);
		cir.setReturnValue(PlayerTeam.formatNameForTeam(team, display));
	}

	private static PlayerTeam getTeamForPlayer(String playerName) {
		Minecraft mc = Minecraft.getInstance();
		if (mc.level == null) return null;
		Scoreboard scoreboard = mc.level.getScoreboard();
		return scoreboard.getPlayersTeam(playerName);
	}

	private static String findNicknameByUUID(UUID uuid) {
		Minecraft mc = Minecraft.getInstance();
		if (mc.level == null) return null;
		for (Player player : mc.level.players()) {
			if (player.getUUID().equals(uuid)) {
				Component customName = player.getCustomName();
				return customName != null ? customName.getString() : null;
			}
		}
		return null;
	}
}
