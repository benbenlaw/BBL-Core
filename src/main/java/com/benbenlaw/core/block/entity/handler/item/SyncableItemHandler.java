package com.benbenlaw.core.block.entity.handler.item;

import com.benbenlaw.core.block.entity.SyncableBlockEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ItemStacksResourceHandler;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;

import java.util.function.BiPredicate;
import java.util.function.Predicate;

public class SyncableItemHandler extends ItemStacksResourceHandler {

    private final BiPredicate<Integer, ItemStack> canInsert;
    private final Predicate<Integer> canExtract;
    private final SyncableBlockEntity blockEntity;

    private boolean internalMode = false;

    public SyncableItemHandler(SyncableBlockEntity blockEntity, int size, BiPredicate<Integer, ItemStack> canInsert, Predicate<Integer> canExtract) {
        super(size);
        this.blockEntity = blockEntity;
        this.canInsert = canInsert;
        this.canExtract = canExtract;
    }

    @Override
    public int insert(int index, ItemResource resource, int amount, TransactionContext tx) {
        if (resource.isEmpty()) return 0;

        ItemStack stack = resource.toStack(amount);

        if (internalMode || canInsert.test(index, stack)) {
            return super.insert(index, resource, amount, tx);
        }

        return 0;
    }

    @Override
    public int extract(int index, ItemResource resource, int amount, TransactionContext tx) {
        if (resource.isEmpty()) return 0;

        if (internalMode || canExtract.test(index)) {
            return super.extract(index, resource, amount, tx);
        }

        return 0;
    }

    @Override
    public boolean isValid(int index, ItemResource resource) {
        if (internalMode) return true;

        return canInsert.test(index, resource.toStack(1)) && super.isValid(index, resource);
    }

    @Override
    protected void onContentsChanged(int index, ItemStack previousContents) {
        blockEntity.setChanged();
        blockEntity.sync();
    }

    public <T> T runInternal(java.util.function.Supplier<T> action) {
        boolean prev = internalMode;
        internalMode = true;
        try {
            return action.get();
        } finally {
            internalMode = prev;
        }
    }

    public void runInternal(Runnable action) {
        boolean prev = internalMode;
        internalMode = true;
        try {
            action.run();
        } finally {
            internalMode = prev;
        }
    }

}