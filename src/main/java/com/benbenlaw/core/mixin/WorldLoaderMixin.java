package com.benbenlaw.core.mixin;

import com.benbenlaw.core.world.WorldInfoCache;
import com.ibm.icu.impl.Pair;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.LayeredRegistryAccess;
import net.minecraft.core.RegistryAccess;
import net.minecraft.server.RegistryLayer;
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
import java.util.concurrent.Executor;

@Mixin(WorldLoader.class)
public class WorldLoaderMixin {

    @Inject(
            method = "load",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/ReloadableServerResources;loadResources(" +
                            "Lnet/minecraft/server/packs/resources/ResourceManager;" +
                            "Lnet/minecraft/core/LayeredRegistryAccess;" +
                            "Ljava/util/List;" +
                            "Lnet/minecraft/world/flag/FeatureFlagSet;" +
                            "Lnet/minecraft/commands/Commands$CommandSelection;" +
                            "ILjava/util/concurrent/Executor;" +
                            "Ljava/util/concurrent/Executor;)Ljava/util/concurrent/CompletableFuture;"
            )
    )
    private static <D, R> void captureWorldDataBeforeFactory(
            WorldLoader.InitConfig initConfig,
            WorldLoader.WorldDataSupplier<D> worldDataSupplier,
            WorldLoader.ResultFactory<D, R> resultFactory,
            Executor backgroundExecutor,
            Executor gameExecutor,
            CallbackInfoReturnable<CompletableFuture<R>> cir,
            // List<Registry.PendingTags<?>>
            // List<HolderLookup.RegistryLookup<?>>
            @Local WorldLoader.DataLoadOutput<D> dataloadoutput
    ) {
        WorldInfoCache.capture(dataloadoutput.finalDimensions());
    }
}
