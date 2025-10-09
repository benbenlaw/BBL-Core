package com.benbenlaw.core.block.entity.handler.item;

import com.benbenlaw.core.block.entity.SyncableBlockEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ItemStacksResourceHandler;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;

import java.util.function.BiPredicate;

public class FilterItemHandler extends ItemStacksResourceHandler {

    private final SyncableBlockEntity blockEntity;

    public FilterItemHandler(SyncableBlockEntity blockEntity, int size) {
        super(size);
        this.blockEntity = blockEntity;
    }

    @Override
    public boolean isValid(int index, ItemResource resource) {
        return false;
    }

    @Override
    public int insert(int index, ItemResource resource, int amount, TransactionContext transaction) {
        return 0;
    }

    @Override
    public int extract(int index, ItemResource resource, int amount, TransactionContext transaction) {
        return 0;
    }

    @Override
    protected void onContentsChanged(int index, ItemStack previousContents) {
        blockEntity.setChanged();
        blockEntity.sync();
    }
}