package com.benbenlaw.core.mixin.client;

import com.benbenlaw.core.client.BBLCoreClient;
import com.mojang.serialization.Lifecycle;
import net.minecraft.client.gui.screens.worldselection.CreateWorldScreen;
import net.minecraft.core.LayeredRegistryAccess;
import net.minecraft.server.RegistryLayer;
import net.minecraft.world.level.storage.PrimaryLevelData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CreateWorldScreen.class)
public class CreateWorldScreenMixin {
    @Inject(at = @At("HEAD"), method = "createNewWorld")
    private void interceptCreation(
            PrimaryLevelData.SpecialWorldProperty specialWorldProperty, LayeredRegistryAccess<RegistryLayer> p_249152_, Lifecycle worldGenSettingsLifecycle,
            CallbackInfo ci
    ) {
        BBLCoreClient.isCreatingNewWorld = true;
    }
}
