package com.benbenlaw.core.mixin;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.level.LevelHeightAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = ClientLevel.ClientLevelData.class)
public class HorizonHeightMixin {

    @Inject(method = "getHorizonHeight", at = @At("HEAD"), cancellable = true)
    public void getHorizonHeight(LevelHeightAccessor p_171688_, CallbackInfoReturnable<Double> cir) {
        cir.setReturnValue(-100.0D);
    }
}
