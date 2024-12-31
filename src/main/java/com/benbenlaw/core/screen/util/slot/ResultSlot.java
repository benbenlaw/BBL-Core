package com.benbenlaw.core.screen.util.slot;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

public class ResultSlot extends SlotItemHandler {
    private final int slotMaxStackSize;

    public ResultSlot(IItemHandler itemHandler, int index, int x, int y, int slotMaxStackSize) {
        super(itemHandler, index, x, y);
        this.slotMaxStackSize = slotMaxStackSize;
    }

    @Override
    public boolean mayPlace(@NotNull ItemStack stack) {
        return false;
    }

    @Override
    public int getMaxStackSize() {
        return slotMaxStackSize;
    }
}