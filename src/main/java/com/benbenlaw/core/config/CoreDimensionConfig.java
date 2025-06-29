package com.benbenlaw.core.config;

import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.Collections;
import java.util.List;

public class CoreDimensionConfig {

    public static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
    public static final ModConfigSpec SPEC;
    public static final ModConfigSpec.ConfigValue<List<? extends String>> dimensionNames;  // list of strings


    static {

        // Dimensions Config
        BUILDER.comment("Dimension Configuration Settings");

        // Dimensions
        BUILDER.push("Allows beds to work in other dimensions and progress the night to day");

        dimensionNames = BUILDER.defineList(
                "Dimensions add like this: minecraft:the_nether in quotes",
                Collections.emptyList(),
                () -> "",
                element -> element instanceof String
        );

        BUILDER.pop();


        //LAST
        SPEC = BUILDER.build();

    }

}
