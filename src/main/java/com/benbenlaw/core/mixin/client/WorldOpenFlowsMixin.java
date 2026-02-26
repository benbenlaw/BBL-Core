package com.benbenlaw.core.mixin.client;

import com.benbenlaw.core.world.WorldInfoCache;
import net.minecraft.client.gui.screens.worldselection.WorldOpenFlows;
import net.minecraft.core.LayeredRegistryAccess;
import net.minecraft.server.RegistryLayer;
import net.minecraft.server.ReloadableServerResources;
import net.minecraft.world.level.gamerules.GameRules;
import net.minecraft.world.level.storage.LevelDataAndDimensions;
import net.minecraft.world.level.storage.LevelStorageSource;
import net.minecraft.world.level.storage.WorldData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(WorldOpenFlows.class)
public abstract class WorldOpenFlowsMixin {

    @Inject(method = "createLevelFromExistingSettings", at = @At("HEAD"))
    private void captureNewWorldType(
            LevelStorageSource.LevelStorageAccess levelSourceAccess, ReloadableServerResources serverResources, LayeredRegistryAccess<RegistryLayer> registryAccess, LevelDataAndDimensions.WorldDataAndGenSettings worldDataAndGenSettings, Optional<GameRules> gameRules, CallbackInfo ci
    ) {
        WorldInfoCache.capture(registryAccess.compositeAccess());
    }
}
