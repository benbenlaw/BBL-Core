package com.benbenlaw.core.config;

import net.neoforged.neoforge.common.ModConfigSpec;

public class ColorTintIndexConfig {

    public static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
    public static final ModConfigSpec SPEC;

    public static final ModConfigSpec.ConfigValue<Integer> whiteTintIndex;
    public static final ModConfigSpec.ConfigValue<Integer> blackTintIndex;
    public static final ModConfigSpec.ConfigValue<Integer> redTintIndex;
    public static final ModConfigSpec.ConfigValue<Integer> greenTintIndex;
    public static final ModConfigSpec.ConfigValue<Integer> blueTintIndex;
    public static final ModConfigSpec.ConfigValue<Integer> yellowTintIndex;
    public static final ModConfigSpec.ConfigValue<Integer> cyanTintIndex;
    public static final ModConfigSpec.ConfigValue<Integer> magentaTintIndex;
    public static final ModConfigSpec.ConfigValue<Integer> orangeTintIndex;
    public static final ModConfigSpec.ConfigValue<Integer> limeTintIndex;
    public static final ModConfigSpec.ConfigValue<Integer> pinkTintIndex;
    public static final ModConfigSpec.ConfigValue<Integer> grayTintIndex;
    public static final ModConfigSpec.ConfigValue<Integer> lightGrayTintIndex;
    public static final ModConfigSpec.ConfigValue<Integer> lightBlueTintIndex;
    public static final ModConfigSpec.ConfigValue<Integer> brownTintIndex;
    public static final ModConfigSpec.ConfigValue<Integer> purpleTintIndex;



    static {

        // Tint Index Config
        BUILDER.comment("Tint Index Config")
                .comment("Controls the color tint applied per color across the blocks, items and names.")
                .comment("Can be in Hex or Decimal value show in the config.")
                .comment("This website can help you find the color you want https://www.colorhexa.com/.")
                .push("Tint Index");

        whiteTintIndex = BUILDER.define("White Tint Index, default = 0xFFFFFF", 0xFFFFFF);
        blackTintIndex = BUILDER.define("Black Tint Index, default = 0x37393C", 0x37393C);
        redTintIndex = BUILDER.define("Red Tint Index, default = 0xD04D45", 0xD04D45);
        greenTintIndex = BUILDER.define("Green Tint Index, default = 0x546E17", 0x546E17);
        blueTintIndex = BUILDER.define("Blue Tint Index, default = 0x0096FF", 0x0096FF);
        yellowTintIndex = BUILDER.define("Yellow Tint Index, default = 0xFFFF55", 0xFFFF55);
        cyanTintIndex = BUILDER.define("Cyan Tint Index, default = 0x13828B", 0x13828B);
        magentaTintIndex = BUILDER.define("Magenta Tint Index, default = 0xE26EDE", 0xE26EDE);
        orangeTintIndex = BUILDER.define("Orange Tint Index, default = 0xF59029", 0xE9680A);
        limeTintIndex = BUILDER.define("Lime Tint Index, default = 0x39FA39", 0x39FA39);
        pinkTintIndex = BUILDER.define("Pink Tint Index, default = 0xEAA5C9", 0xEAA5C9);
        grayTintIndex = BUILDER.define("Gray Tint Index, default = 0x5A6063", 0x5A6063);
        lightGrayTintIndex = BUILDER.define("Light Gray Tint Index, default = 0xAAAAA3", 0xAAAAA3);
        lightBlueTintIndex = BUILDER.define("Light Blue Tint Index, default = 0x5CD2F3", 0x5CD2F3);
        brownTintIndex = BUILDER.define("Brown Tint Index, default = 0x805230", 0x805230);
        purpleTintIndex = BUILDER.define("Purple Tint Index, default = 0x8F3BBE", 0x8F3BBE);

        BUILDER.pop();

        //LAST

        SPEC = BUILDER.build();
    }
}
