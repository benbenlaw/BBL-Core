package com.benbenlaw.core.mixin.client;

import com.benbenlaw.core.client.BBLCoreClient;
import com.mojang.serialization.Lifecycle;
import net.minecraft.client.gui.screens.worldselection.CreateWorldScreen;
import net.minecraft.core.LayeredRegistryAccess;
import net.minecraft.server.RegistryLayer;
import net.minecraft.world.level.storage.PrimaryLevelData;
import net.minecraft.world.level.storage.WorldData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CreateWorldScreen.class)
public class CreateWorldScreenMixin {
    @Inject(at = @At("HEAD"), method = "createNewWorld")
    private void interceptCreation(
            LayeredRegistryAccess<RegistryLayer> registryAccess, WorldData worldData, CallbackInfoReturnable<Boolean> cir
    ) {
        BBLCoreClient.isCreatingNewWorld = true;
    }
}
