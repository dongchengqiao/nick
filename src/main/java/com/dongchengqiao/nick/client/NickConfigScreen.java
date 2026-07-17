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
			.setTitle(Component.translatable("nick.config.title"));

		ConfigEntryBuilder entryBuilder = builder.entryBuilder();

		ConfigCategory category = builder.getOrCreateCategory(
			Component.translatable("nick.config.category.display"));
		category.addEntry(entryBuilder.startTextDescription(
			Component.translatable("nick.config.description.selector_note")
		).build());
		category.addEntry(entryBuilder
			.startSelector(
				Component.translatable("nick.config.default_mode"),
				new DisplayMode[]{DisplayMode.NICK_ONLY, DisplayMode.NICK_AND_ORIGINAL, DisplayMode.HIDE},
				NickClientConfig.getDefaultMode()
			)
			.setDefaultValue(DisplayMode.NICK_ONLY)
			.setSaveConsumer(NickClientConfig::setDefaultMode)
			.setNameProvider(m -> Component.translatable(((DisplayMode) m).getTranslationKey()))
			.build());

		var subCategory = entryBuilder.startSubCategory(Component.translatable("nick.config.location_settings"));
		for (DisplayLocation loc : DisplayLocation.values()) {
			DisplayMode current = NickClientConfig.getOverride(loc);
			subCategory.add(entryBuilder
				.startEnumSelector(
					Component.translatable(loc.getTranslationKey()),
					DisplayMode.class,
					current != null ? current : DisplayMode.DEFAULT
				)
				.setDefaultValue(DisplayMode.DEFAULT)
				.setSaveConsumer(mode -> NickClientConfig.setOverride(loc, mode))
				.setEnumNameProvider(m -> Component.translatable(((DisplayMode) m).getTranslationKey()))
				.build());
		}
		category.addEntry(subCategory.build());

		return builder.build();
	}
}
