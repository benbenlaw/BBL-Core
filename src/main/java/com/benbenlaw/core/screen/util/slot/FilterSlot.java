package com.benbenlaw.core.screen.util.slot;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

public class FilterSlot extends SlotItemHandler {
    public FilterSlot(IItemHandler itemHandler, int index, int x, int y) {
        super(itemHandler, index, x, y);
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
    public void set(@NotNull ItemStack stack) {
        if (!stack.isEmpty()) {
            super.set(stack.copyWithCount(1));
        } else {
            super.set(ItemStack.EMPTY);
        }
    }
}
