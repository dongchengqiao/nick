package com.dongchengqiao.nick.mixin;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.arguments.selector.EntitySelectorParser;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(EntitySelectorParser.class)
public class NickEntitySelectorParserMixin {
	@Redirect(method = "parseNameOrUUID", at = @At(value = "INVOKE", target = "Lcom/mojang/brigadier/StringReader;readString()Ljava/lang/String;"))
	private String readStringWithUnicode(StringReader reader) throws CommandSyntaxException {
		if (!reader.canRead()) {
			return "";
		}
		char next = reader.peek();
		if (StringReader.isQuotedStringStart(next)) {
			return reader.readString();
		}
		int start = reader.getCursor();
		while (reader.canRead()) {
			char c = reader.peek();
			if (!StringReader.isAllowedInUnquotedString(c) && c <= 127) {
				break;
			}
			reader.skip();
		}
		return reader.getString().substring(start, reader.getCursor());
	}
}
