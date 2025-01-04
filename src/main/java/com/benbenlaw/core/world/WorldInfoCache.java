package com.benbenlaw.core.world;

import com.benbenlaw.core.Core;
import com.benbenlaw.core.mixin.access.ChunkGeneratorAccess;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.dimension.LevelStem;

public class WorldInfoCache {
    private static Holder<MapCodec<? extends ChunkGenerator>> type;

    public static void capture(RegistryAccess access) {
        var reg = access.lookup(Registries.LEVEL_STEM).orElse(null);
        if (reg == null) return;

        var overworld = reg.get(LevelStem.OVERWORLD).map(Holder::value).orElse(null);
        if (overworld == null) return;

        type = BuiltInRegistries.CHUNK_GENERATOR.wrapAsHolder(((ChunkGeneratorAccess) overworld.generator()).bblcore$getCodec());

        Core.LOGGER.info("Captured world type as using generator " + type.getRegisteredName());
    }

    public static boolean matches(HolderSet<MapCodec<? extends ChunkGenerator>> other) {
        return type != null && other.contains(type);
    }
}
