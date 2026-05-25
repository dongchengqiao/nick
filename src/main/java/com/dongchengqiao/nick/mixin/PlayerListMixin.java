package com.dongchengqiao.nick.mixin;

import com.dongchengqiao.nick.NickConfig;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(PlayerList.class)
public class PlayerListMixin {
	@Shadow
	private List<ServerPlayer> players;

	@Inject(method = "getPlayerByName", at = @At("TAIL"), cancellable = true)
	private void onGetPlayerByName(String name, CallbackInfoReturnable<ServerPlayer> cir) {
		if (cir.getReturnValue() == null) {
			String username = NickConfig.getPlayerByNick(name);
			if (username != null) {
				for (ServerPlayer player : players) {
					if (player.getGameProfile().name().equalsIgnoreCase(username)) {
						cir.setReturnValue(player);
						return;
					}
				}
			}
		}
	}

}
