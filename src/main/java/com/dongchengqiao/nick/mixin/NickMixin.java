package com.dongchengqiao.nick.mixin;

import com.dongchengqiao.nick.NickConfig;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.scores.PlayerTeam;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Player.class)
public abstract class NickMixin {
	@Inject(method = "getDisplayName", at = @At("HEAD"), cancellable = true)
	private void onGetDisplayName(CallbackInfoReturnable<Component> cir) {
		Player player = (Player)(Object)this;
		String nick = NickConfig.getNick(player.getScoreboardName());
		if (nick != null) {
			MutableComponent name = PlayerTeam.formatNameForTeam(player.getTeam(), Component.literal(nick));
			cir.setReturnValue(invokeDecorateDisplayNameComponent(name));
		}
	}

	@Invoker("decorateDisplayNameComponent")
	abstract MutableComponent invokeDecorateDisplayNameComponent(MutableComponent base);
}
