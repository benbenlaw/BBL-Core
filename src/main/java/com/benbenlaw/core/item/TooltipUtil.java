package com.benbenlaw.core.item;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class TooltipUtil {

    public static void addShiftTooltip(List<Component> list, String shiftHeldKey) {
        if (Screen.hasShiftDown()) {
            list.add(Component.translatable(shiftHeldKey).withStyle(ChatFormatting.YELLOW));
        } else {
            list.add(Component.translatable("tooltips.bblcore.shift").withStyle(ChatFormatting.YELLOW));
        }
    }
    public static void addAltTooltip(ItemStack stack, List<Component> list, String shiftHeldKey) {
        if (Screen.hasAltDown()) {
            list.add(Component.translatable(shiftHeldKey).withStyle(ChatFormatting.YELLOW));
        } else {
            list.add(Component.translatable("tooltips.bblcore.alt").withStyle(ChatFormatting.YELLOW));
        }
    }
    public static void addControlTooltip(ItemStack stack, List<Component> list, String shiftHeldKey) {
        if (Screen.hasControlDown()) {
            list.add(Component.translatable(shiftHeldKey).withStyle(ChatFormatting.YELLOW));
        } else {
            list.add(Component.translatable("ttooltips.bblcore.ctrl").withStyle(ChatFormatting.YELLOW));
        }
    }

}

