package com.benbenlaw.core.config;

import net.neoforged.neoforge.common.ModConfigSpec;

public class StartupConfig {

    public static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
    public static final ModConfigSpec SPEC;
    public static final ModConfigSpec.ConfigValue<Boolean> enableSaveTheWorld;
    public static final ModConfigSpec.ConfigValue<Integer> positionToFixX;
    public static final ModConfigSpec.ConfigValue<Integer> positionToFixY;
    public static final ModConfigSpec.ConfigValue<Integer> positionToFixZ;

    static {

        //Save The World
        BUILDER.comment("BBL Core Startup Config")
                .push("BBL Core");

        enableSaveTheWorld = BUILDER.comment("Save the world will try to fix broken world where a block is causing crashes, default = false")
                .define("Try to Save the world", false);

        positionToFixX = BUILDER.comment("X coordinate of block to fix (default = 0)")
                .define("x", 0);
        positionToFixY = BUILDER.comment("Y coordinate of block to fix (default = 0)")
                .define("y", 0);
        positionToFixZ = BUILDER.comment("Z coordinate of block to fix (default = 0)")
                .define("z", 0);

        BUILDER.pop();

        //LAST
        SPEC = BUILDER.build();

    }

}
