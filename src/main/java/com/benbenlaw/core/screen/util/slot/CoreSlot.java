package com.benbenlaw.core.screen.util.slot;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.SlotItemHandler;

public class CoreSlot extends SlotItemHandler {

    public CoreSlot(IItemHandler itemHandler, int index, int xPos, int yPos) {
        super(itemHandler, index, xPos, yPos);
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        if (stack.isEmpty())
            return false;
        return getItemHandler().isItemValid(index, stack);
    }
}
