package com.dongchengqiao.nick;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import java.util.Collection;
import java.util.List;

public class UnicodeWordArgumentType implements ArgumentType<String> {
	public UnicodeWordArgumentType() {}

	public static UnicodeWordArgumentType unicodeWord() {
		return new UnicodeWordArgumentType();
	}

	@Override
	public String parse(StringReader reader) throws CommandSyntaxException {
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

	@Override
	public Collection<String> getExamples() {
		return List.of("word", "玩家", "示例");
	}
}
