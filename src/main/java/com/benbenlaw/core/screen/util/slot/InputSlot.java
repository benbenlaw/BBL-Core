package com.benbenlaw.core.screen.util.slot;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.transfer.IndexModifier;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ResourceHandlerSlot;

public class InputSlot extends ResourceHandlerSlot {

    private int maxStackSize = -1; // -1 = use default

    public InputSlot(ResourceHandler<ItemResource> handler, IndexModifier<ItemResource> slotModifier, int index, int xPosition, int yPosition) {
        super(handler, slotModifier, index, xPosition, yPosition);
    }

    /** Allows setting a custom max stack size */
    public InputSlot size(int maxStackSize) {
        this.maxStackSize = maxStackSize;
        return this;
    }

    @Override
    public boolean mayPickup(Player player) {
        return true;
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return true;
    }

    @Override
    public int getMaxStackSize() {
        return maxStackSize > 0 ? maxStackSize : super.getMaxStackSize();
    }

    @Override
    public int getMaxStackSize(ItemStack stack) {
        return maxStackSize > 0 ? maxStackSize : super.getMaxStackSize(stack);
    }
}