package com.benbenlaw.core.mixin;

import com.benbenlaw.core.world.WorldInfoCache;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.LayeredRegistryAccess;
import net.minecraft.core.RegistryAccess;
import net.minecraft.server.ReloadableServerResources;
import net.minecraft.server.WorldLoader;
import net.minecraft.server.packs.resources.CloseableResourceManager;
import net.minecraft.world.level.WorldDataConfiguration;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Executor;


@Mixin(WorldLoader.class)
public class WorldLoaderMixin {

    @Inject(
            method = "lambda$load$2",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/core/LayeredRegistryAccess;replaceFrom(Ljava/lang/Object;[Lnet/minecraft/core/RegistryAccess$Frozen;)Lnet/minecraft/core/LayeredRegistryAccess;"
            )
    )
    private static void captureDimensions(
            // Use @Local to grab the variable without needing the full signature
            Pair packsAndResourceManager, List dimensionContextRegistries, WorldLoader.WorldDataSupplier worldDataSupplier, CloseableResourceManager resources, LayeredRegistryAccess initialLayers, RegistryAccess.Frozen loadedWorldgenRegistries, List staticLayerTags, WorldLoader.InitConfig config, Executor backgroundExecutor, Executor mainThreadExecutor, WorldLoader.ResultFactory resultFactory, RegistryAccess.Frozen initialWorldgenDimensions, CallbackInfoReturnable<CompletionStage> cir, @Local WorldLoader.DataLoadOutput<?> worldDataAndRegistries
    ) {
        WorldInfoCache.capture(worldDataAndRegistries.finalDimensions());
    }
}