package com.dongchengqiao.nick;

import com.mojang.brigadier.arguments.StringArgumentType;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket;
import net.minecraft.server.level.ServerPlayer;

import static net.minecraft.commands.Commands.*;

public class NickCommand {
	public static void register() {
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
			dispatcher.register(literal("nick")
				.then(literal("set")
					.then(argument("name", StringArgumentType.string())
						.executes(ctx -> {
							ServerPlayer player = ctx.getSource().getPlayerOrException();
							String nick = StringArgumentType.getString(ctx, "name");
							setNick(player, nick);
							ctx.getSource().sendSuccess(() -> Component.literal("§a昵称已设置为: " + nick), false);
							return 1;
						}))
					.then(argument("target", EntityArgument.player())
						.requires(hasPermission(LEVEL_MODERATORS))
						.then(argument("name", StringArgumentType.string())
							.executes(ctx -> {
								ServerPlayer target = EntityArgument.getPlayer(ctx, "target");
								String nick = StringArgumentType.getString(ctx, "name");
								setNick(target, nick);
								ctx.getSource().sendSuccess(() -> Component.literal("§a已设置 " + target.getScoreboardName() + " 的昵称为: " + nick), false);
								return 1;
							}))))
				.then(literal("reset")
					.executes(ctx -> {
						ServerPlayer player = ctx.getSource().getPlayerOrException();
						resetNick(player);
						ctx.getSource().sendSuccess(() -> Component.literal("§a昵称已重置"), false);
						return 1;
					})
					.then(argument("target", EntityArgument.player())
						.requires(hasPermission(LEVEL_MODERATORS))
						.executes(ctx -> {
							ServerPlayer target = EntityArgument.getPlayer(ctx, "target");
							resetNick(target);
							ctx.getSource().sendSuccess(() -> Component.literal("§a已重置 " + target.getScoreboardName() + " 的昵称"), false);
							return 1;
						})))
			);
		});
	}

	private static void setNick(ServerPlayer player, String nick) {
		NickConfig.setNick(player.getScoreboardName(), nick);
		player.setCustomName(Component.literal(nick));
		player.setCustomNameVisible(true);
		updateTabList(player);
	}

	private static void resetNick(ServerPlayer player) {
		NickConfig.removeNick(player.getScoreboardName());
		player.setCustomName(null);
		player.setCustomNameVisible(false);
		updateTabList(player);
	}

	private static void updateTabList(ServerPlayer player) {
		player.level().getServer().getPlayerList().broadcastAll(
			new ClientboundPlayerInfoUpdatePacket(
				ClientboundPlayerInfoUpdatePacket.Action.UPDATE_DISPLAY_NAME, player
			)
		);
	}
}
