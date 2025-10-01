package com.benbenlaw.core.screen.util;

import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.network.chat.Component;

public abstract class CoreWidget extends AbstractWidget {
    public final int x;
    public final int y;
    public final int width;
    public final int height;

    public CoreWidget(int x, int y, int width, int height) {
        super(x, y, width, height, Component.empty());
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }
}