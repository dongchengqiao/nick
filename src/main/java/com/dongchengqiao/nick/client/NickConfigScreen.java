package com.dongchengqiao.nick.client;

import com.dongchengqiao.nick.NickClientConfig;
import com.dongchengqiao.nick.NickClientConfig.DisplayMode;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

@Environment(EnvType.CLIENT)
public class NickConfigScreen extends Screen {
	private final Screen parent;
	private StringWidget label;
	private CycleButton<DisplayMode> cycleButton;

	public NickConfigScreen(Screen parent) {
		super(Component.literal("Nick 配置"));
		this.parent = parent;
	}

	@Override
	protected void init() {
		int midX = width / 2;
		int labelWidth = font.width("显示模式：");
		int totalWidth = labelWidth + 5 + 150;
		int startX = midX - totalWidth / 2;

		label = new StringWidget(Component.literal("显示模式："), font);
		label.setX(startX);
		label.setY(44);
		addRenderableWidget(label);

		cycleButton = CycleButton.builder(
				(DisplayMode mode) -> Component.literal(mode.getDisplayName()),
				NickClientConfig.getDisplayMode()
			)
			.withValues(DisplayMode.values())
			.displayOnlyValue()
			.create(startX + labelWidth + 5, 40, 150, 20,
				Component.literal("显示模式"),
				(btn, value) -> NickClientConfig.setDisplayMode(value));
		addRenderableWidget(cycleButton);

		addRenderableWidget(Button.builder(
				Component.literal("完成"),
				btn -> onClose()
			)
			.bounds(midX - 50, height - 40, 100, 20)
			.build());
	}

	@Override
	public void extractRenderState(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float delta) {
		super.extractRenderState(guiGraphics, mouseX, mouseY, delta);
		guiGraphics.centeredText(font, title, width / 2, 8, 0xFFFFFF);
	}

	@Override
	public void onClose() {
		minecraft.setScreen(parent);
	}
}
