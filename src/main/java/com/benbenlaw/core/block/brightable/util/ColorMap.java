package com.benbenlaw.core.block.brightable.util;

import com.benbenlaw.core.config.ColorTintIndexConfig;
import net.minecraft.world.item.DyeColor;

import java.util.HashMap;

public class ColorMap {

    //This Has Map coverts the DyeColor to the RGB value for the tinting of blocks and items both inworld and in inventory
    public static final HashMap<DyeColor, Integer> COLOR_MAP = new HashMap<>() {{

        put(DyeColor.WHITE, ColorTintIndexConfig.whiteTintIndex.get());
        put(DyeColor.ORANGE, ColorTintIndexConfig.orangeTintIndex.get());
        put(DyeColor.LIGHT_BLUE, ColorTintIndexConfig.lightBlueTintIndex.get());
        put(DyeColor.CYAN, ColorTintIndexConfig.cyanTintIndex.get());
        put(DyeColor.PINK, ColorTintIndexConfig.pinkTintIndex.get());
        put(DyeColor.YELLOW, ColorTintIndexConfig.yellowTintIndex.get());
        put(DyeColor.LIGHT_GRAY, ColorTintIndexConfig.lightGrayTintIndex.get());
        put(DyeColor.GRAY, ColorTintIndexConfig.grayTintIndex.get());
        put(DyeColor.RED, ColorTintIndexConfig.redTintIndex.get());
        put(DyeColor.LIME, ColorTintIndexConfig.limeTintIndex.get());
        put(DyeColor.BLUE, ColorTintIndexConfig.blueTintIndex.get());
        put(DyeColor.PURPLE, ColorTintIndexConfig.purpleTintIndex.get());
        put(DyeColor.BROWN, ColorTintIndexConfig.brownTintIndex.get());
        put(DyeColor.GREEN, ColorTintIndexConfig.greenTintIndex.get());
        put(DyeColor.BLACK, ColorTintIndexConfig.blackTintIndex.get());
        put(DyeColor.MAGENTA, ColorTintIndexConfig.magentaTintIndex.get());

        /* old hardcoded values
        put(DyeColor.WHITE, (0xFFFFFF));
        put(DyeColor.ORANGE, (0xE9680A));
        put(DyeColor.LIGHT_BLUE, (0x0096FF));;
        put(DyeColor.CYAN, (0x13828B));
        put(DyeColor.PINK, (0xEAA5C9));
        put(DyeColor.YELLOW, (0xFFFF55));
        put(DyeColor.LIGHT_GRAY, (0xAAAAA3));
        put(DyeColor.GRAY, (0x5A6063));
        put(DyeColor.RED, (0xD04D45));
        put(DyeColor.LIME, (0x39FA39));
        put(DyeColor.BLUE, (0x5555FF));
        put(DyeColor.PURPLE, (0x8F3BBE));
        put(DyeColor.BROWN, (0x805230));
        put(DyeColor.GREEN, (0x546E17));
        put(DyeColor.BLACK, (0x37393C));
        put(DyeColor.MAGENTA, (0xE26EDE));

         */
    }};

    private static final HashMap<String, DyeColor> STRING_TO_DYE_COLOR = new HashMap<>() {{
        put("white", DyeColor.WHITE);
        put("orange", DyeColor.ORANGE);
        put("light_blue", DyeColor.LIGHT_BLUE);
        put("cyan", DyeColor.CYAN);
        put("pink", DyeColor.PINK);
        put("yellow", DyeColor.YELLOW);
        put("light_gray", DyeColor.LIGHT_GRAY);
        put("gray", DyeColor.GRAY);
        put("red", DyeColor.RED);
        put("lime", DyeColor.LIME);
        put("blue", DyeColor.BLUE);
        put("purple", DyeColor.PURPLE);
        put("brown", DyeColor.BROWN);
        put("green", DyeColor.GREEN);
        put("black", DyeColor.BLACK);
        put("magenta", DyeColor.MAGENTA);
    }};
    private static final HashMap<String, String> STRING_TO_STRING_TRANSLATION = new HashMap<>() {{
        put("white", "color.bblcore.white");
        put("orange", "color.bblcore.orange");
        put("light_blue", "color.bblcore.light_blue");
        put("cyan", "color.bblcore.cyan");
        put("pink", "color.bblcore.pink");
        put("yellow", "color.bblcore.yellow");
        put("light_gray", "color.bblcore.light_gray");
        put("gray", "color.bblcore.gray");
        put("red", "color.bblcore.red");
        put("lime", "color.bblcore.lime");
        put("blue", "color.bblcore.blue");
        put("purple", "color.bblcore.purple");
        put("brown", "color.bblcore.brown");
        put("green", "color.bblcore.green");
        put("black", "color.bblcore.black");
        put("magenta", "color.bblcore.magenta");
    }};

    public static Integer getColorValue(DyeColor dyeColor) {
        return COLOR_MAP.get(dyeColor);
    }

    public static DyeColor getDyeColor(String colorName) {
        return STRING_TO_DYE_COLOR.get(colorName.toLowerCase());
    }

    public static String getTranslationKey(String colorName) {
        return STRING_TO_STRING_TRANSLATION.get(colorName);
    }

}