package com.benbenlaw.core.screen.util.slot;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.transfer.IndexModifier;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ResourceHandlerSlot;

public class WhitelistSlot extends ResourceHandlerSlot {

    private final ItemStack itemLike;

    public WhitelistSlot(ResourceHandler<ItemResource> handler, IndexModifier<ItemResource> slotModifier, int index, int xPosition, int yPosition, ItemStack itemLike) {
        super(handler, slotModifier, index, xPosition, yPosition);
        this.itemLike = itemLike;
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return stack.is(itemLike.getItem());
    }
}
