package com.benbenlaw.core.config;

import net.neoforged.neoforge.common.ModConfigSpec;

public class CoreStartupConfig {

    public static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
    public static final ModConfigSpec SPEC;
    public static final ModConfigSpec.ConfigValue<Double> climbableBlockSpeed;
    public static final ModConfigSpec.ConfigValue<Boolean> coloringRecipes;
    public static final ModConfigSpec.ConfigValue<Boolean> enabledVoidProtection;

    static {

        // Caveopolis Configs
        BUILDER.comment("BBL Core Startup Config")
                .push("BBL Core");

        climbableBlockSpeed = BUILDER.comment("Speed that climbable blocks can be climbed, default = 0.15")
                .define("Climbable Block Speed", 0.15);

        coloringRecipes = BUILDER.comment("Enable coloring recipes when coloring item are present, eg BBL Colors")
                        .comment("Uses item names and checks for 'color' in the name, if the item contains a color, coloring items can change it to that color")
                .define("Coloring Recipes in Crafting Tables", true);

        enabledVoidProtection = BUILDER.comment("Enable Void Protection, default = false")
                .comment("If enabled, players will be teleported to their spawn point if they fall into the void")
                .define("Enable Void Protection", false);

        BUILDER.pop();


        //LAST
        SPEC = BUILDER.build();

    }

}
