package com.benbenlaw.core.recipe;

import com.benbenlaw.core.world.WorldInfoCache;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.neoforged.neoforge.common.conditions.ICondition;
import org.jetbrains.annotations.NotNull;

public record WorldTypeCondition(HolderSet<MapCodec<? extends ChunkGenerator>> worldTypes) implements ICondition {

    public static final MapCodec<WorldTypeCondition> CODEC = RegistryCodecs.homogeneousList(Registries.CHUNK_GENERATOR)
            .fieldOf("world_type").xmap(WorldTypeCondition::new, WorldTypeCondition::worldTypes);
    @Override
    public MapCodec<? extends ICondition> codec() {
        return CODEC;
    }

    @Override
    public boolean test(@NotNull IContext context) {
        return WorldInfoCache.matches(worldTypes);
    }
    @Override
    public String toString() {
        return "Valid World Type (" + worldTypes + ")";
    }
}
