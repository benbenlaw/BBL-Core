package com.benbenlaw.core.util;

import net.minecraft.world.item.DyeColor;

public enum ColorUtils {
    WHITE("white", DyeColor.WHITE),
    ORANGE("orange", DyeColor.ORANGE),
    MAGENTA("magenta", DyeColor.MAGENTA),
    LIGHT_BLUE("light_blue", DyeColor.LIGHT_BLUE),
    YELLOW("yellow", DyeColor.YELLOW),
    LIME("lime", DyeColor.LIME),
    PINK("pink", DyeColor.PINK),
    GRAY("gray", DyeColor.GRAY),
    LIGHT_GRAY("light_gray", DyeColor.LIGHT_GRAY),
    CYAN("cyan", DyeColor.CYAN),
    PURPLE("purple", DyeColor.PURPLE),
    BLUE("blue", DyeColor.BLUE),
    BROWN("brown", DyeColor.BROWN),
    GREEN("green", DyeColor.GREEN),
    RED("red", DyeColor.RED),
    BLACK("black", DyeColor.BLACK);

    private final String name;
    private final DyeColor dyeColor;

    ColorUtils(String name, DyeColor dyeColor) {
        this.name = name;
        this.dyeColor = dyeColor;
    }
    /// Returns the name of the color
    public String getName() {
        return name;
    }
    /// Returns the corresponding DyeColor
    public DyeColor getDyeColor() {
        return dyeColor;
    }
    /// Used for text color in GUIs
    public int getTextColor() {
        return dyeColor.getTextColor();
    }
    /// This is best used for tinting textures
    public int getTextureColor() {
        return dyeColor.getTextureDiffuseColor();
    }
}
