package com.dongchengqiao.nick.mixin;

import com.dongchengqiao.nick.NickConfig;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayer.class)
public class NickTabListMixin {
	@Inject(method = "getTabListDisplayName", at = @At("HEAD"), cancellable = true)
	private void onGetTabListDisplayName(CallbackInfoReturnable<Component> cir) {
		String nick = NickConfig.getNick(((Player)(Object)this).getScoreboardName());
		if (nick != null) {
			cir.setReturnValue(Component.literal(nick));
		}
	}
}
