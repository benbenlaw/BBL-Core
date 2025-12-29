package com.benbenlaw.core.screen.util;

import com.benbenlaw.core.Core;
import net.minecraft.client.gui.components.WidgetSprites;

public class CoreButtons {

    public static final WidgetSprites ON_BUTTONS = new WidgetSprites(
            Core.identifier("machine/on"),
            Core.identifier("machine/on_hover")
    );

    public static final WidgetSprites OFF_BUTTONS = new WidgetSprites(
            Core.identifier( "machine/off"),
            Core.identifier( "machine/off_hover")
    );

    public static final WidgetSprites INCREASE_BUTTONS = new WidgetSprites(
            Core.identifier( "machine/increase"),
            Core.identifier( "machine/increase_hover")
    );

    public static final WidgetSprites DECREASE_BUTTONS = new WidgetSprites(
            Core.identifier( "machine/decrease"),
            Core.identifier( "machine/decrease_hover")
    );

    public static final WidgetSprites SAVED_RECIPE_BUTTONS = new WidgetSprites(
            Core.identifier( "machine/save_recipe"),
            Core.identifier( "machine/save_recipe_hover")
    );



}


