package com.dongchengqiao.nick.mixin;

import com.dongchengqiao.nick.NickClientConfig;
import com.dongchengqiao.nick.NickClientConfig.DisplayLocation;
import com.dongchengqiao.nick.NickClientConfig.DisplayMode;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
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
		Component customName = player.getCustomName();
		if (customName == null) {
			return null;
		}
		String nick = customName.getString();
		DisplayMode mode = NickClientConfig.getDisplayMode(DisplayLocation.NAMETAG);
		Component display;
		switch (mode) {
			case HIDE:
				display = Component.literal(player.getScoreboardName());
				break;
			case NICK_AND_ORIGINAL:
				display = Component.literal("[" + nick + "]" + player.getScoreboardName());
				break;
			case NICK_ONLY:
			default:
				display = Component.literal(nick);
				break;
		}
		return PlayerTeam.formatNameForTeam(player.getTeam(), display);
	}
}
