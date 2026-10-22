package com.dongchengqiao.nick.mixin;

import com.dongchengqiao.nick.NickSettings;
import com.mojang.brigadier.StringReader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(StringReader.class)
public class NickStringReaderMixin {
	@Inject(method = "isAllowedInUnquotedString", at = @At("RETURN"), cancellable = true, remap = false)
	private static void allowUnicodeInUnquotedString(char c, CallbackInfoReturnable<Boolean> cir) {
		if (NickSettings.unicodeArgumentsSupport) {
			cir.setReturnValue(cir.getReturnValueZ() || Character.isLetterOrDigit(c) || c > 0x7F);
		}
	}
}
