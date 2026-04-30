package com.benbenlaw.core.block.entity.handler.fluid;

import com.benbenlaw.core.block.entity.SyncableBlockEntity;
import com.benbenlaw.core.block.entity.handler.item.SyncableItemHandler;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.fluid.FluidStacksResourceHandler;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;

import java.util.function.BiPredicate;
import java.util.function.Predicate;

public class SyncableFluidHandler extends FluidStacksResourceHandler {

    private final SyncableBlockEntity blockEntity;
    private final BiPredicate<Integer, FluidStack> canInsert;
    private final Predicate<Integer> canExtract;

    private boolean internalMode = false;

    public SyncableFluidHandler(SyncableBlockEntity blockEntity, int size, int capacity, BiPredicate<Integer, FluidStack> canInsert, Predicate<Integer> canExtract) {
        super(size, capacity);
        this.blockEntity = blockEntity;
        this.canInsert = canInsert;
        this.canExtract = canExtract;
    }

    @Override
    public int insert(int index, FluidResource resource, int amount, TransactionContext tx) {
        if (resource.isEmpty()) return 0;

        FluidStack stack = resource.toStack(amount);

        if (internalMode || canInsert.test(index, stack)) {
            return super.insert(index, resource, amount, tx);
        }

        return 0;
    }

    @Override
    public int extract(int index, FluidResource resource, int amount, TransactionContext tx) {
        if (resource.isEmpty()) return 0;

        if (internalMode || canExtract.test(index)) {
            return super.extract(index, resource, amount, tx);
        }

        return 0;
    }

    @Override
    public boolean isValid(int index, FluidResource resource) {
        if (internalMode) return true;

        return canInsert.test(index, resource.toStack(1)) && super.isValid(index, resource);
    }

    @Override
    protected void onContentsChanged(int index, FluidStack previousContents) {
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