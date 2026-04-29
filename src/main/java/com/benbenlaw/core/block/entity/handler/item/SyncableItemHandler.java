package com.benbenlaw.core.block.entity.handler.item;

import com.benbenlaw.core.block.entity.SyncableBlockEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.transfer.item.ItemStacksResourceHandler;

public class SyncableItemHandler extends ItemStacksResourceHandler {

    SyncableBlockEntity blockEntity;

    public SyncableItemHandler(SyncableBlockEntity blockEntity, int size) {
        super(size);
        this.blockEntity = blockEntity;
    }

    @Override
    protected void onContentsChanged(int index, ItemStack previousContents) {
        super.onContentsChanged(index, previousContents);
        blockEntity.setChanged();
        blockEntity.sync();
    }
}
