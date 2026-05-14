package com.github.dedinc.demodisabler.mixin;

import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Minecraft.class)
public class MinecraftMixin {
    @Inject(method = "isDemo", at = @At("HEAD"), cancellable = true)
    private void onIsDemo(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(false);
    }
}
