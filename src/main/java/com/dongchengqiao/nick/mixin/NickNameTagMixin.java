package com.dongchengqiao.nick.mixin;

import com.dongchengqiao.nick.NickClientConfig;
import com.dongchengqiao.nick.NickClientConfig.DisplayLocation;
import com.dongchengqiao.nick.NickClientConfig.DisplayMode;
import com.dongchengqiao.nick.client.NickClientUtil;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.scores.PlayerTeam;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityRenderer.class)
public class NickNameTagMixin {
	@Inject(method = "getNameTag", at = @At("HEAD"), cancellable = true)
	private void onGetNameTag(Entity entity, CallbackInfoReturnable<Component> cir) {
		if (entity instanceof Player player) {
			Component display = buildDisplay(player);
			if (display != null) {
				cir.setReturnValue(display);
			}
		}
	}

	private static Component buildDisplay(Player player) {
		DisplayMode mode = NickClientConfig.getDisplayMode(DisplayLocation.NAMETAG);
		if (mode == DisplayMode.DEFAULT) {
			return null;
		}
		String originalName = player.getGameProfile().name();
		String nick = NickClientUtil.getNickname(player);
		Component display;
		switch (mode) {
			case HIDE:
				display = Component.literal(originalName);
				break;
			case NICK_AND_ORIGINAL:
				if (nick == null) {
					return null;
				}
				display = Component.literal("[" + nick + "]" + originalName);
				break;
			case NICK_ONLY:
				if (nick == null) {
					return null;
				}
				display = Component.literal(nick);
				break;
			default:
				return null;
		}
		return PlayerTeam.formatNameForTeam(player.getTeam(), display);
	}
}
