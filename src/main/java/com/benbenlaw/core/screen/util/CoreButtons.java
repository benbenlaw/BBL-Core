package com.benbenlaw.core.screen.util;

import com.benbenlaw.core.Core;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public class CoreButtons {

    @OnlyIn(Dist.CLIENT)
    public static final WidgetSprites ON_BUTTONS = new WidgetSprites(
            ResourceLocation.fromNamespaceAndPath(Core.MOD_ID, "machine/on"),
            ResourceLocation.fromNamespaceAndPath(Core.MOD_ID, "machine/on_hover")
    );

    public static final WidgetSprites OFF_BUTTONS = new WidgetSprites(
            ResourceLocation.fromNamespaceAndPath(Core.MOD_ID, "machine/off"),
            ResourceLocation.fromNamespaceAndPath(Core.MOD_ID, "machine/off_hover")
    );

    public static final WidgetSprites INCREASE_BUTTONS = new WidgetSprites(
            ResourceLocation.fromNamespaceAndPath(Core.MOD_ID, "machine/increase"),
            ResourceLocation.fromNamespaceAndPath(Core.MOD_ID, "machine/increase_hover")
    );

    public static final WidgetSprites DECREASE_BUTTONS = new WidgetSprites(
            ResourceLocation.fromNamespaceAndPath(Core.MOD_ID, "machine/decrease"),
            ResourceLocation.fromNamespaceAndPath(Core.MOD_ID, "machine/decrease_hover")
    );

    public static final WidgetSprites SAVED_RECIPE_BUTTONS = new WidgetSprites(
            ResourceLocation.fromNamespaceAndPath(Core.MOD_ID, "machine/save_recipe"),
            ResourceLocation.fromNamespaceAndPath(Core.MOD_ID, "machine/save_recipe_hover")
    );



}


