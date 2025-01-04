package com.benbenlaw.core.mixin;

import com.benbenlaw.core.world.WorldInfoCache;
import com.mojang.serialization.Dynamic;
import net.minecraft.client.gui.screens.worldselection.WorldOpenFlows;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.WorldStem;
import net.minecraft.server.packs.repository.PackRepository;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.storage.LevelStorageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(WorldOpenFlows.class)
public abstract class WorldOpenFlowsMixin {
    @Inject(method = "loadWorldStem",at = @At("RETURN"), cancellable = true)

    private void captureWorldType(Dynamic<?> dynamic, boolean safeMode, PackRepository packRepository, CallbackInfoReturnable<WorldStem> cir) {
        System.out.println("Injection triggered!");
        WorldStem worldStem = cir.getReturnValue();
        if (worldStem != null) {

            LevelStem levelStem = BBLCore$getLevelStemFromWorldStem(worldStem).orElse(null);
            assert levelStem != null;
            String worldType = levelStem.generator().getTypeNameForDataFixer()
                    .toString()
                    .replace("Optional[ResourceKey[minecraft:worldgen/chunk_generator / ", "")
                    .replace("]]", "");

            System.out.println("Captured world type: " + worldType);
            WorldInfoCache.setWorldType(worldType);
            cir.setReturnValue(worldStem);
        } else {
            System.out.println("World stem is null!");
        }
    }


    @Unique
    private Optional<LevelStem> BBLCore$getLevelStemFromWorldStem(WorldStem worldStem) {

        Optional<Registry<LevelStem>> optionalRegistry = worldStem.registries().compositeAccess().registry(Registries.LEVEL_STEM);

        if (optionalRegistry.isPresent()) {
            Registry<LevelStem> levelStemRegistry = optionalRegistry.get();

            ResourceKey<LevelStem> defaultKey = ResourceKey.create(Registries.LEVEL_STEM, ResourceLocation.fromNamespaceAndPath("minecraft", "overworld"));
            return Optional.ofNullable(levelStemRegistry.get(defaultKey));
        }

        return Optional.empty();
    }
}

