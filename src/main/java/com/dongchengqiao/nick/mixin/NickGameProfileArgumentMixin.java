package com.dongchengqiao.nick.mixin;

import carpet.CarpetServer;
import net.minecraft.commands.arguments.GameProfileArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.NameAndId;
import net.minecraft.server.players.UserNameToIdResolver;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Optional;

@Mixin(GameProfileArgument.class)
public class NickGameProfileArgumentMixin {
	@Redirect(
		method = "lambda$parse$*",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/server/players/UserNameToIdResolver;get(Ljava/lang/String;)Ljava/util/Optional;"
		),
		require = 0
	)
	private static Optional<NameAndId> resolveByNickName(UserNameToIdResolver resolver, String name) {
		Optional<NameAndId> result = resolver.get(name);
		if (result.isPresent() || CarpetServer.minecraft_server == null) {
			return result;
		}
		for (ServerPlayer player : CarpetServer.minecraft_server.getPlayerList().getPlayers()) {
			Component customName = player.getCustomName();
			if (customName != null && customName.getString().equalsIgnoreCase(name)) {
				return Optional.of(player.nameAndId());
			}
		}
		return Optional.empty();
	}
}
