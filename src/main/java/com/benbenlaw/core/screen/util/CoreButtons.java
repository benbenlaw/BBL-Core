package com.benbenlaw.core.screen.util;

import com.benbenlaw.core.Core;
import net.minecraft.client.gui.components.WidgetSprites;

public class CoreButtons {

    public static final WidgetSprites ON_BUTTONS = new WidgetSprites(
            Core.rl("machine/on"),
            Core.rl("machine/on_hover")
    );

    public static final WidgetSprites OFF_BUTTONS = new WidgetSprites(
            Core.rl( "machine/off"),
            Core.rl( "machine/off_hover")
    );

    public static final WidgetSprites INCREASE_BUTTONS = new WidgetSprites(
            Core.rl( "machine/increase"),
            Core.rl( "machine/increase_hover")
    );

    public static final WidgetSprites DECREASE_BUTTONS = new WidgetSprites(
            Core.rl( "machine/decrease"),
            Core.rl( "machine/decrease_hover")
    );

    public static final WidgetSprites SAVED_RECIPE_BUTTONS = new WidgetSprites(
            Core.rl( "machine/save_recipe"),
            Core.rl( "machine/save_recipe_hover")
    );



}


