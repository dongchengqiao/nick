package com.dongchengqiao.nick.mixin;


import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(PlayerList.class)
public class NickSuggestionsMixin {
	@Shadow
	private List<ServerPlayer> players;

	@Inject(method = "getPlayerNamesArray", at = @At("HEAD"), cancellable = true)
	private void onGetPlayerNamesArray(CallbackInfoReturnable<String[]> cir) {
		String[] result = new String[players.size()];
		for (int i = 0; i < players.size(); i++) {
			ServerPlayer player = players.get(i);
			Component customName = player.getCustomName();
			if (customName != null) {
				result[i] = customName.getString();
			} else {
				result[i] = player.getScoreboardName();
			}
		}
		cir.setReturnValue(result);
	}
}
