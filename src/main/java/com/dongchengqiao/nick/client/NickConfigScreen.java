package com.dongchengqiao.nick.client;

import com.dongchengqiao.nick.NickClientConfig;
import com.dongchengqiao.nick.NickClientConfig.DisplayMode;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

@Environment(EnvType.CLIENT)
public class NickConfigScreen {
	public static Screen create(Screen parent) {
		ConfigBuilder builder = ConfigBuilder.create()
			.setParentScreen(parent)
			.setTitle(Component.literal("Nick 配置"));

		ConfigCategory category = builder.getOrCreateCategory(Component.literal("显示设置"));

		ConfigEntryBuilder entryBuilder = builder.entryBuilder();

		category.addEntry(entryBuilder
			.startEnumSelector(
				Component.literal("显示模式"),
				DisplayMode.class,
				NickClientConfig.getDisplayMode()
			)
			.setDefaultValue(DisplayMode.NICK_ONLY)
			.setSaveConsumer(NickClientConfig::setDisplayMode)
			.setEnumNameProvider(mode -> Component.literal(((DisplayMode) mode).getDisplayName()))
			.build());

		return builder.build();
	}
}
