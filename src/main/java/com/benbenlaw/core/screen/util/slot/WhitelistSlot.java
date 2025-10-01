package com.benbenlaw.core.screen.util.slot;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.SlotItemHandler;

public class WhitelistSlot extends SlotItemHandler {

    private final ItemStack itemLike;

    public WhitelistSlot(IItemHandler itemHandler, int index, int xPos, int yPos, ItemStack itemLike) {
        super(itemHandler, index, xPos, yPos);
        this.itemLike = itemLike;
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return stack.is(itemLike.getItem());
    }
}
