package com.benbenlaw.core.config;

import net.neoforged.neoforge.common.ModConfigSpec;

public class CoreStartupConfig {

    public static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
    public static final ModConfigSpec SPEC;
    public static final ModConfigSpec.ConfigValue<Double> climbableBlockSpeed;

    static {

        // Caveopolis Configs
        BUILDER.comment("BBL Core Startup Config")
                .push("BBL Core");

        climbableBlockSpeed = BUILDER.comment("Speed that climbable blocks can be climbed, default = 0.15")
                .define("Climbable Block Speed", 0.15);




        BUILDER.pop();



        //LAST
        SPEC = BUILDER.build();

    }

}
