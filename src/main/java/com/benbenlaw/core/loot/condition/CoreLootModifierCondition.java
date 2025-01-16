package com.benbenlaw.core.loot.condition;

import com.benbenlaw.core.Core;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class CoreLootModifierCondition {

    public static final DeferredRegister<LootItemConditionType> LOOT_CONDITION_TYPES =
            DeferredRegister.create(Registries.LOOT_CONDITION_TYPE, Core.MOD_ID);

    public static final DeferredHolder<LootItemConditionType, LootItemConditionType> WORLD_TYPE_CONDITION =
            LOOT_CONDITION_TYPES.register("world_type_condition", () -> new LootItemConditionType(WorldTypeLootCondition.CODEC));


    public static void register(IEventBus eventBus) {
        LOOT_CONDITION_TYPES.register(eventBus);
    }

}
