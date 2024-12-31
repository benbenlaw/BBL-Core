package com.benbenlaw.core.screen.util;

public class TooltipArea {
    public int offsetX, offsetY, width, height;
    public String translationKey;

    public TooltipArea(int offsetX, int offsetY, int width, int height, String translationKey) {
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.width = width;
        this.height = height;
        this.translationKey = translationKey;
    }
}