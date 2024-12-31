package com.benbenlaw.core.block.entity.handler;

import net.neoforged.neoforge.items.ItemStackHandler;

public interface IInventoryHandlingBlockEntity {
    void setHandler(ItemStackHandler handler);
    ItemStackHandler getItemStackHandler();
}