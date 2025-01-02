package com.benbenlaw.core.recipe;

import com.benbenlaw.core.Core;
import com.mojang.serialization.MapCodec;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.conditions.ICondition;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.apache.logging.log4j.core.pattern.MapPatternConverter;

public class CoreConditions {

    public static final DeferredRegister<MapCodec<? extends ICondition>> CONDITIONALS =
            DeferredRegister.create(NeoForgeRegistries.Keys.CONDITION_CODECS, Core.MOD_ID);

    public static final DeferredHolder<MapCodec<? extends ICondition>, MapCodec<WorldTypeCondition>> WORLD_TYPE_CONDITION =
            CONDITIONALS.register("world_type_condition", () -> WorldTypeCondition.CODEC);

    public static void register(IEventBus eventBus) {
        CONDITIONALS.register(eventBus);
    }


}
