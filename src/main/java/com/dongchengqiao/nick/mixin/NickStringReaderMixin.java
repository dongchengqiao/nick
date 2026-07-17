package com.dongchengqiao.nick.mixin;

import com.dongchengqiao.nick.NickSettings;
import com.mojang.brigadier.StringReader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(StringReader.class)
public class NickStringReaderMixin {
	@Inject(method = "isAllowedInUnquotedString", at = @At("RETURN"), cancellable = true)
	private static void allowUnicodeInUnquotedString(char c, CallbackInfoReturnable<Boolean> cir) {
		if (!cir.getReturnValue() && NickSettings.commandPlayerCN && c > 127) {
			cir.setReturnValue(true);
		}
	}
}
