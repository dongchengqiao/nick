package com.dongchengqiao.nick.mixin;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.scores.PlayerTeam;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayer.class)
public abstract class NickTabListMixin {
	@Inject(method = "getTabListDisplayName", at = @At("HEAD"), cancellable = true)
	private void onGetTabListDisplayName(CallbackInfoReturnable<Component> cir) {
		Player player = (Player)(Object)this;
		Component customName = player.getCustomName();
		if (customName == null) {
			return;
		}
		MutableComponent result = PlayerTeam.formatNameForTeam(player.getTeam(), customName);
		cir.setReturnValue(((NickMixin)(Object)player).invokeDecorateDisplayNameComponent(result));
	}
}
