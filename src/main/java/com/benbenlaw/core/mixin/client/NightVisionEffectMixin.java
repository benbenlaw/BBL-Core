package com.benbenlaw.core.mixin.client;

import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
@Mixin(GameRenderer.class)
public abstract class NightVisionEffectMixin {

    @Inject(method = "getNightVisionScale", at = @At("HEAD"), cancellable = true)
    private static void getNightVisionScale(LivingEntity livingEntity, float nanoTime, CallbackInfoReturnable<Float> cir) {
        MobEffectInstance mobeffectinstance = livingEntity.getEffect(MobEffects.NIGHT_VISION);
        if (mobeffectinstance != null) {
            cir.setReturnValue(1.0F);
        } else {
            cir.setReturnValue(0.0F);
        }
    }
}
