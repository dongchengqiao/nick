package com.dongchengqiao.nick;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket;
import net.minecraft.server.level.ServerPlayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Nick implements ModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("nick");
	@Override
	public void onInitialize() {
		NickConfig.load();
		NickCommand.register();

		ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
			ServerPlayer player = handler.player;
			String username = player.getScoreboardName();
			String nick = NickConfig.getNick(username);
			if (nick != null) {
				player.setCustomName(Component.literal(nick));
				player.setCustomNameVisible(true);
				player.level().getServer().getPlayerList().broadcastAll(
					new ClientboundPlayerInfoUpdatePacket(
						ClientboundPlayerInfoUpdatePacket.Action.UPDATE_DISPLAY_NAME, player
					)
				);
			}
		});
	}
}
