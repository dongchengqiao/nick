package com.dongchengqiao.nick.mixin;

import net.minecraft.network.chat.MutableComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(net.minecraft.world.entity.player.Player.class)
public abstract class PlayerMixin {
	@Invoker("decorateDisplayNameComponent")
	public abstract MutableComponent invokeDecorateDisplayNameComponent(MutableComponent base);
}
