package com.benbenlaw.core.recipe;

import com.benbenlaw.core.config.CoreStartupConfig;
import com.mojang.serialization.MapCodec;
import net.neoforged.neoforge.common.conditions.ICondition;

public class CraftingTableApplyColor implements ICondition {

    public static final CraftingTableApplyColor INSTANCE = new CraftingTableApplyColor();
    public static final MapCodec<CraftingTableApplyColor> CODEC = MapCodec.unit(INSTANCE);

    @Override
    public boolean test(IContext context) {
        return CoreStartupConfig.coloringRecipes.get();
    }

    @Override
    public MapCodec<? extends ICondition> codec() {
        return CODEC;
    }
}
