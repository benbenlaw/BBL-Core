package com.benbenlaw.core.mixin.client;

import com.benbenlaw.core.world.WorldInfoCache;
import net.minecraft.client.gui.screens.worldselection.WorldOpenFlows;
import net.minecraft.core.LayeredRegistryAccess;
import net.minecraft.server.RegistryLayer;
import net.minecraft.server.ReloadableServerResources;
import net.minecraft.world.level.storage.LevelStorageSource;
import net.minecraft.world.level.storage.WorldData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldOpenFlows.class)
public abstract class WorldOpenFlowsMixin {
    @Inject(method = "createLevelFromExistingSettings", at = @At("HEAD"))
    private void captureNewWorldType(
            LevelStorageSource.LevelStorageAccess levelStorage,
            ReloadableServerResources resources,
            LayeredRegistryAccess<RegistryLayer> registries,
            WorldData worldData,
            CallbackInfo ci
    ) {
        WorldInfoCache.capture(registries.compositeAccess());
    }
}

