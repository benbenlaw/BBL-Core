package com.benbenlaw.core.screen.util.slot;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.SlotItemHandler;
import net.neoforged.neoforge.transfer.IndexModifier;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ResourceHandlerSlot;
import org.jetbrains.annotations.NotNull;

public class FilterSlot extends ResourceHandlerSlot {


    public FilterSlot(ResourceHandler<ItemResource> handler, IndexModifier<ItemResource> slotModifier, int index, int xPosition, int yPosition) {
        super(handler, slotModifier, index, xPosition, yPosition);
    }


    @Override
    public boolean mayPlace(@NotNull ItemStack stack) {
        return true;
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }

    @Override
    public boolean mayPickup(@NotNull Player player) {
        return true;
    }

    @Override
    public void onTake(@NotNull Player player, @NotNull ItemStack stack) {
        this.set(ItemStack.EMPTY);
    }

    @Override
    public void setByPlayer(ItemStack stack) {
        if (!stack.isEmpty()) {
            super.set(stack.copyWithCount(1));
        } else {
            super.set(ItemStack.EMPTY);
        }
    }
}
