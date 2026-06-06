package com.dongchengqiao.nick.mixin;

import com.dongchengqiao.nick.NickClientConfig;
import com.dongchengqiao.nick.NickClientConfig.DisplayLocation;
import com.dongchengqiao.nick.NickClientConfig.DisplayMode;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
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
		if (mode == DisplayMode.DEFAULT || mode == DisplayMode.NICK_ONLY) {
			return;
		}

		String originalName = ((PlayerInfo)(Object)this).getProfile().name();

		if (mode == DisplayMode.HIDE) {
			cir.setReturnValue(Component.literal(originalName));
			return;
		}

		UUID uuid = ((PlayerInfo)(Object)this).getProfile().id();
		String nickname = findNicknameByUUID(uuid);
		if (nickname != null) {
			cir.setReturnValue(Component.literal("[" + nickname + "]" + originalName));
		}
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
