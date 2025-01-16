package com.benbenlaw.core.loot.condition;

import com.benbenlaw.core.world.WorldInfoCache;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import org.jetbrains.annotations.NotNull;

public record WorldTypeLootCondition(HolderSet<MapCodec<? extends ChunkGenerator>> worldTypes) implements LootItemCondition {

    public static final MapCodec<WorldTypeLootCondition> CODEC = RegistryCodecs.homogeneousList(Registries.CHUNK_GENERATOR)
            .fieldOf("world_type").xmap(WorldTypeLootCondition::new, WorldTypeLootCondition::worldTypes);

    @Override
    public @NotNull LootItemConditionType getType() {
        return CoreLootModifierCondition.WORLD_TYPE_CONDITION.get();
    }
    @Override
    public boolean test(LootContext lootContext) {
        return WorldInfoCache.matches(worldTypes);
    }
}
