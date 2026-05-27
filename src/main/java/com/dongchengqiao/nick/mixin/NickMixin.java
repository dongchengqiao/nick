package com.dongchengqiao.nick.mixin;

import com.dongchengqiao.nick.NickClientConfig;
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

		Component customName = player.getCustomName();
		if (customName == null) {
			return;
		}

		String nick = customName.getString();
		String original = player.getScoreboardName();

		Component display;
		switch (NickClientConfig.getDisplayMode()) {
			case HIDE:
				display = Component.literal(original);
				break;
			case NICK_AND_ORIGINAL:
				display = Component.literal("[" + nick + "]" + original);
				break;
			case NICK_ONLY:
			default:
				display = Component.literal(nick);
				break;
		}

		MutableComponent name = PlayerTeam.formatNameForTeam(player.getTeam(), display);
		cir.setReturnValue(invokeDecorateDisplayNameComponent(name));
	}

	@Invoker("decorateDisplayNameComponent")
	abstract MutableComponent invokeDecorateDisplayNameComponent(MutableComponent base);
}
