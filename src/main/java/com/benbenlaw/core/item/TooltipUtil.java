package com.benbenlaw.core.item;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.TooltipDisplay;

import java.util.function.Consumer;

public class TooltipUtil {

    public static void addShiftTooltip(TooltipDisplay display, Consumer<Component> tooltipAdder, Component shiftHeldKey) {
        if (Screen.hasShiftDown()) {
            tooltipAdder.accept(shiftHeldKey.copy().withStyle(ChatFormatting.YELLOW));
        } else {
            tooltipAdder.accept(Component.translatable("tooltips.bblcore.shift").withStyle(ChatFormatting.YELLOW));
        }
    }

    public static void addAltTooltip(ItemStack stack, TooltipDisplay display, Consumer<Component> tooltipAdder, Component altHeldKey) {
        if (Screen.hasAltDown()) {
            tooltipAdder.accept(altHeldKey.copy().withStyle(ChatFormatting.YELLOW));
        } else {
            tooltipAdder.accept(Component.translatable("tooltips.bblcore.alt").withStyle(ChatFormatting.YELLOW));
        }
    }

    public static void addControlTooltip(ItemStack stack, TooltipDisplay display, Consumer<Component> tooltipAdder, Component ctrlHeldKey) {
        if (Screen.hasControlDown()) {
            tooltipAdder.accept(ctrlHeldKey.copy().withStyle(ChatFormatting.YELLOW));
        } else {
            tooltipAdder.accept(Component.translatable("tooltips.bblcore.ctrl").withStyle(ChatFormatting.YELLOW));
        }
    }
}


