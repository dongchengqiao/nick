package com.dongchengqiao.nick.mixin;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.scores.Scoreboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Entity.class)
public class NickEntityMixin {
	@Redirect(method = "getTeam", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/scores/Scoreboard;getPlayersTeam(Ljava/lang/String;)Lnet/minecraft/world/scores/PlayerTeam;"))
	private PlayerTeam redirectGetPlayersTeam(Scoreboard scoreboard, String playerName) {
		if ((Object)this instanceof Player player) {
			return scoreboard.getPlayersTeam(player.getGameProfile().name());
		}
		return scoreboard.getPlayersTeam(playerName);
	}
}
