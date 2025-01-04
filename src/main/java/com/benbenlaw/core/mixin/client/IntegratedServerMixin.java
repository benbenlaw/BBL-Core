package com.benbenlaw.core.mixin.client;

import com.benbenlaw.core.Core;
import com.benbenlaw.core.client.BBLCoreClient;
import com.mojang.datafixers.DataFixer;
import net.minecraft.client.server.IntegratedServer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.Services;
import net.minecraft.server.WorldStem;
import net.minecraft.server.level.progress.ChunkProgressListenerFactory;
import net.minecraft.server.packs.repository.PackRepository;
import net.minecraft.world.level.storage.LevelStorageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.net.Proxy;

@Mixin(IntegratedServer.class)
public abstract class IntegratedServerMixin extends MinecraftServer {
    public IntegratedServerMixin(Thread serverThread, LevelStorageSource.LevelStorageAccess storageSource, PackRepository packRepository, WorldStem worldStem, Proxy proxy, DataFixer fixerUpper, Services services, ChunkProgressListenerFactory progressListenerFactory) {
        super(serverThread, storageSource, packRepository, worldStem, proxy, fixerUpper, services, progressListenerFactory);
    }

    @Inject(at = @At("TAIL"), method = "initServer")
    private void forceReload(CallbackInfoReturnable<Boolean> ci) {
        if (BBLCoreClient.isCreatingNewWorld) {
            Core.LOGGER.info("Detected newly created world... forcing pack reload");

            reloadResources(getPackRepository().getSelectedIds()).exceptionally(ex -> {
                Core.LOGGER.warn("Failed to execute reload", ex);
                return null;
            });

            BBLCoreClient.isCreatingNewWorld = false;
        }
    }
}
