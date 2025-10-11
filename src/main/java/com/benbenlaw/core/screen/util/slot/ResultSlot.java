package com.benbenlaw.core.screen.util.slot;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.transfer.IndexModifier;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ResourceHandlerSlot;
import org.jetbrains.annotations.NotNull;

public class ResultSlot extends ResourceHandlerSlot {

    private int slotMaxStackSize = -1; // -1 = use default

    public ResultSlot(ResourceHandler<ItemResource> handler, IndexModifier<ItemResource> slotModifier, int index, int xPosition, int yPosition) {
        super(handler, slotModifier, index, xPosition, yPosition);
    }

    /** Allows setting a custom max stack size */
    public ResultSlot size(int maxStackSize) {
        this.slotMaxStackSize = maxStackSize;
        return this;
    }

    @Override
    public boolean mayPlace(@NotNull ItemStack stack) {
        return false;
    }

    @Override
    public int getMaxStackSize() {
        return slotMaxStackSize > 0 ? slotMaxStackSize : super.getMaxStackSize();
    }

    @Override
    public int getMaxStackSize(ItemStack stack) {
        return slotMaxStackSize > 0 ? slotMaxStackSize : super.getMaxStackSize(stack);
    }
}