package com.benbenlaw.core.screen.util.slot;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.transfer.IndexModifier;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ResourceHandlerSlot;
import org.jetbrains.annotations.NotNull;

public class ResultSlot extends ResourceHandlerSlot {

    private final ResourceHandler<ItemResource> handler;
    private final int slotIndex;
    private int maxStackSize = -1;

    public ResultSlot(ResourceHandler<ItemResource> handler, IndexModifier<ItemResource> slotModifier, int index, int xPosition, int yPosition) {
        super(handler, slotModifier, index, xPosition, yPosition);
        this.handler = handler;
        this.slotIndex = index;
    }

    /** Allows setting a custom max stack size */
    public ResultSlot size(int maxStackSize) {
        this.maxStackSize = maxStackSize;
        return this;
    }

    @Override
    public boolean mayPlace(@NotNull ItemStack stack) {
        return false;
    }

    @Override
    public int getMaxStackSize() {
        return maxStackSize > 0 ? maxStackSize : super.getMaxStackSize();
    }

    @Override
    public int getMaxStackSize(@NotNull ItemStack stack) {
        int handlerCap = handler.getCapacityAsInt(slotIndex, ItemResource.of(stack));
        int cap = Math.min(handlerCap, stack.getMaxStackSize());
        return maxStackSize > 0 ? Math.min(maxStackSize, cap) : cap;
    }
}