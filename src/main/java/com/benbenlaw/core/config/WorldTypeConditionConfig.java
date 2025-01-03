package com.benbenlaw.core.config;

import net.neoforged.neoforge.common.ModConfigSpec;

public class WorldTypeConditionConfig {

    public static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
    public static final ModConfigSpec SPEC;
    public static final ModConfigSpec.ConfigValue<Boolean> enableRecipeReload;


    static {

        // Tint Index Config
        BUILDER.comment("World Type Recipes Config")
                .comment("Enabling this will force the first client joining a world to trigger a recipe reload.")
                .comment("This allows the World Type Condition do be loaded into the game.")
                .comment("If not using world type conditions leave this off.")
                .push("World Type Condition");

        enableRecipeReload = BUILDER.define("Enable Recipe Reload, default = false", false);

        BUILDER.pop();

        //LAST

        SPEC = BUILDER.build();
    }
}
