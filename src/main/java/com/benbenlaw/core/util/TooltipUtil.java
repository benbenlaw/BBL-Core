package com.benbenlaw.core.util;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;

public class TooltipUtil {

    /// Mainly used for adding shift tooltips in event subscribers for ItemTooltipEvent

    public static void addShiftTooltip(ItemStack stack, ItemTooltipEvent event, Item item, String tooltipText) {
        if (!stack.is(item)) return;

        if (Minecraft.getInstance().hasShiftDown()) {
            event.getToolTip().add(
                    Component.translatable(tooltipText).withStyle(ChatFormatting.BLUE)
            );
        } else {
            event.getToolTip().add(
                    Component.translatable("tooltip.bblcore.shift").withStyle(ChatFormatting.YELLOW)
            );
        }
    }
}
