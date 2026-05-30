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
			.setTitle(Component.literal("Nick \u914d\u7f6e"));

		ConfigEntryBuilder entryBuilder = builder.entryBuilder();

		ConfigCategory category = builder.getOrCreateCategory(
			Component.literal("\u663e\u793a\u8bbe\u7f6e"));
		category.addEntry(entryBuilder
			.startSelector(
				Component.literal("\u9ed8\u8ba4\u663e\u793a\u6a21\u5f0f"),
				new DisplayMode[]{DisplayMode.NICK_ONLY, DisplayMode.NICK_AND_ORIGINAL, DisplayMode.HIDE},
				NickClientConfig.getDefaultMode()
			)
			.setDefaultValue(DisplayMode.NICK_ONLY)
			.setSaveConsumer(NickClientConfig::setDefaultMode)
			.setNameProvider(m -> Component.literal(((DisplayMode) m).getDisplayName()))
			.build());

		var subCategory = entryBuilder.startSubCategory(Component.literal("\u4f4d\u7f6e\u663e\u793a\u8bbe\u7f6e"));
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
