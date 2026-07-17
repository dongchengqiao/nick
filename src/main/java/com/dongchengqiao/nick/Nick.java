package com.dongchengqiao.nick;

import carpet.CarpetServer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.ArgumentTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.commands.synchronization.SingletonArgumentInfo;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Nick implements ModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("nick");
	@Override
	public void onInitialize() {
		CarpetServer.manageExtension(new NickExtension());

		ArgumentTypeRegistry.registerArgumentType(
			Identifier.fromNamespaceAndPath("nick", "unicode_word"),
			UnicodeWordArgumentType.class,
			SingletonArgumentInfo.contextFree(UnicodeWordArgumentType::new)
		);

		NickConfig.load();
		NickClientConfig.load();

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
