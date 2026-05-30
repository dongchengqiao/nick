package com.dongchengqiao.nick.mixin;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public class NickSelectorMixin {
	@Inject(method = "getPlainTextName", at = @At("HEAD"), cancellable = true)
	private void onGetPlainTextName(CallbackInfoReturnable<String> cir) {
		Component customName = ((Player)(Object)this).getCustomName();
		if (customName != null) {
			cir.setReturnValue(customName.getString());
		}
	}
}
