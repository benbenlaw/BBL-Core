package com.benbenlaw.core.util;

import net.minecraft.core.Direction;

import java.util.List;

public class ColorList {

    public static final List<String> COLORS = List.of(
            "white", "orange", "magenta", "light_blue", "yellow", "lime", "pink", "gray",
            "light_gray", "cyan", "purple", "blue", "brown", "green", "red", "black"
    );

    public static List<String> getColors() {
        return COLORS;
    }
}


