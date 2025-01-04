package com.benbenlaw.core.mixin;

import com.benbenlaw.core.world.WorldInfoCache;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.server.WorldLoader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Mixin(WorldLoader.class)
public class WorldLoaderMixin {
    @Inject(method = "load", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/ReloadableServerResources;loadResources(Lnet/minecraft/server/packs/resources/ResourceManager;Lnet/minecraft/core/LayeredRegistryAccess;Lnet/minecraft/world/flag/FeatureFlagSet;Lnet/minecraft/commands/Commands$CommandSelection;ILjava/util/concurrent/Executor;Ljava/util/concurrent/Executor;)Ljava/util/concurrent/CompletableFuture;"))
    private static <D, R> void captureWorldData(WorldLoader.InitConfig initConfig,
                                                WorldLoader.WorldDataSupplier<D> worldDataSupplier,
                                                WorldLoader.ResultFactory<D, R> resultFactory,
                                                Executor backgroundExecutor,
                                                Executor gameExecutor,
                                                CallbackInfoReturnable<CompletableFuture<R>> cir,
                                                @Local WorldLoader.DataLoadOutput<D> output) {
        WorldInfoCache.capture(output.finalDimensions());
    }
}
