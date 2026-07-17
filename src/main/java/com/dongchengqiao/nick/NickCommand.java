package com.dongchengqiao.nick;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket;
import net.minecraft.server.level.ServerPlayer;

import static net.minecraft.commands.Commands.*;

public class NickCommand {
	public static void registerCommands(CommandDispatcher<CommandSourceStack> dispatcher) {
		dispatcher.register(literal("nick")
			.requires(src -> NickSettings.commandNick)
			.then(literal("set")
				.then(argument("name", UnicodeWordArgumentType.unicodeWord())
					.executes(ctx -> {
						ServerPlayer player = ctx.getSource().getPlayerOrException();
						String nick = ctx.getArgument("name", String.class);
						setNick(player, nick);
						ctx.getSource().sendSuccess(() -> Component.translatable("nick.command.set", nick), false);
						return 1;
					})
					.then(argument("target", EntityArgument.player())
						.requires(hasPermission(LEVEL_MODERATORS))
						.executes(ctx -> {
							ServerPlayer target = EntityArgument.getPlayer(ctx, "target");
							String nick = ctx.getArgument("name", String.class);
							setNick(target, nick);
							ctx.getSource().sendSuccess(() -> Component.translatable("nick.command.set.other", target.getScoreboardName(), nick), false);
							return 1;
						}))))
			.then(literal("reset")
				.executes(ctx -> {
					ServerPlayer player = ctx.getSource().getPlayerOrException();
					resetNick(player);
					ctx.getSource().sendSuccess(() -> Component.translatable("nick.command.reset"), false);
					return 1;
				})
				.then(argument("target", EntityArgument.player())
					.requires(hasPermission(LEVEL_MODERATORS))
					.executes(ctx -> {
						ServerPlayer target = EntityArgument.getPlayer(ctx, "target");
						resetNick(target);
						ctx.getSource().sendSuccess(() -> Component.translatable("nick.command.reset.other", target.getScoreboardName()), false);
						return 1;
					})))
		);
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
