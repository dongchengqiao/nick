package com.dongchengqiao.nick.client;

import com.dongchengqiao.nick.NickClientConfig;
import com.dongchengqiao.nick.NickClientConfig.DisplayLocation;
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

		ConfigEntryBuilder entryBuilder = builder.entryBuilder();

		ConfigCategory category = builder.getOrCreateCategory(
			Component.literal("显示设置"));
		category.addEntry(entryBuilder.startTextDescription(
			Component.literal("§7目标选择器（如@e[name=...]）不会跟随客户端设置而更改")
		).build());
		category.addEntry(entryBuilder
			.startSelector(
				Component.literal("默认显示模式"),
				new DisplayMode[]{DisplayMode.NICK_ONLY, DisplayMode.NICK_AND_ORIGINAL, DisplayMode.HIDE},
				NickClientConfig.getDefaultMode()
			)
			.setDefaultValue(DisplayMode.NICK_ONLY)
			.setSaveConsumer(NickClientConfig::setDefaultMode)
			.setNameProvider(m -> Component.literal(((DisplayMode) m).getDisplayName()))
			.build());

		var subCategory = entryBuilder.startSubCategory(Component.literal("位置显示设置"));
		for (DisplayLocation loc : DisplayLocation.values()) {
			DisplayMode current = NickClientConfig.getOverride(loc);
			subCategory.add(entryBuilder
				.startEnumSelector(
					Component.literal(loc.getDisplayName()),
					DisplayMode.class,
					current != null ? current : DisplayMode.DEFAULT
				)
				.setDefaultValue(DisplayMode.DEFAULT)
				.setSaveConsumer(mode -> NickClientConfig.setOverride(loc, mode))
				.setEnumNameProvider(m -> Component.literal(((DisplayMode) m).getDisplayName()))
				.build());
		}
		category.addEntry(subCategory.build());

		return builder.build();
	}
}
