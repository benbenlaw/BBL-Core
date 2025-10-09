package com.benbenlaw.core.block.entity.handler.item;

import com.benbenlaw.core.block.entity.SyncableBlockEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ItemStacksResourceHandler;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;

import java.util.function.BiPredicate;

public class InputItemHandler extends ItemStacksResourceHandler {

    private final BiPredicate<Integer, ItemStack> canInsert;
    private final SyncableBlockEntity blockEntity;
    private boolean internalMode = false;

    public InputItemHandler(SyncableBlockEntity blockEntity, int size, BiPredicate<Integer, ItemStack> canInsert) {
        super(size);
        this.canInsert = canInsert;
        this.blockEntity = blockEntity;
    }

    @Override
    public boolean isValid(int index, ItemResource resource) {
        ItemStack stack = resource.toStack(1);
        if (internalMode) {
            return super.isValid(index, resource);
        } else {
            return this.canInsert.test(index, stack) && super.isValid(index, resource);
        }
    }

    @Override
    public int insert(int index, ItemResource resource, int amount, TransactionContext transaction) {
        if (resource.isEmpty()) {
            return 0;
        }

        ItemStack stack = resource.toStack(amount);
        return this.canInsert.test(index, stack)
                ? super.insert(index, resource, amount, transaction)
                : 0;
    }

    @Override
    public int extract(int index, ItemResource resource, int amount, TransactionContext transaction) {
        return 0;
    }

    //Use these internal method to extract without restrictions use for tick methods
    public void extractInternal(int index, ItemResource resource, int amount, TransactionContext transaction) {
        if (resource.isEmpty()) return;

        internalMode = true;
        try {
            super.extract(index, resource, amount, transaction);
        } finally {
            internalMode = false;
        }
    }

    @Override
    protected void onContentsChanged(int index, ItemStack previousContents) {
        blockEntity.setChanged();
        blockEntity.sync();
    }
}