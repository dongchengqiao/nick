package com.dongchengqiao.nick.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientSuggestionProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Mixin(ClientSuggestionProvider.class)
public class NickClientSuggestionsMixin {
	@Shadow
	@Final
	private Minecraft minecraft;

	@Inject(method = "getOnlinePlayerNames", at = @At("RETURN"), cancellable = true)
	private void onGetOnlinePlayerNames(CallbackInfoReturnable<Collection<String>> cir) {
		Collection<String> originalNames = cir.getReturnValue();
		List<String> nicknames = new ArrayList<>(originalNames.size());

		MinecraftServer server = this.minecraft.getSingleplayerServer();
		if (server != null) {
			for (String name : originalNames) {
				ServerPlayer player = server.getPlayerList().getPlayerByName(name);
				if (player != null) {
					Component customName = player.getCustomName();
					if (customName != null) {
						nicknames.add(customName.getString());
					} else {
						nicknames.add(name);
					}
				} else {
					nicknames.add(name);
				}
			}
		} else if (this.minecraft.level != null) {
			for (String name : originalNames) {
				String nickname = name;
				for (Player player : this.minecraft.level.players()) {
					if (player.getGameProfile().name().equals(name)) {
						Component customName = player.getCustomName();
						if (customName != null) {
							nickname = customName.getString();
						}
						break;
					}
				}
				nicknames.add(nickname);
			}
		} else {
			nicknames.addAll(originalNames);
		}

		cir.setReturnValue(nicknames);
	}
}
