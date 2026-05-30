package com.dongchengqiao.nick.mixin;

import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Player.class)
public abstract class NickMixin {
	@Invoker("decorateDisplayNameComponent")
	public abstract MutableComponent invokeDecorateDisplayNameComponent(MutableComponent base);
}
