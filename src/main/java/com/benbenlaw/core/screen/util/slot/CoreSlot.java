package com.benbenlaw.core.screen.util.slot;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.transfer.IndexModifier;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ResourceHandlerSlot;

public class CoreSlot extends ResourceHandlerSlot {

    ResourceHandler<ItemResource> handler;
    int index;

    public CoreSlot(ResourceHandler<ItemResource> handler, IndexModifier<ItemResource> slotModifier, int index, int xPosition, int yPosition) {
        super(handler, slotModifier, index, xPosition, yPosition);
        this.handler = handler;
        this.index = index;
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        if (stack.isEmpty()) return false;
        return handler.isValid(index, ItemResource.of(stack));
    }


}
