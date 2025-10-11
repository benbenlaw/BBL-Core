package com.benbenlaw.core.block.entity.handler.fluid;

import com.benbenlaw.core.block.entity.SyncableBlockEntity;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.fluid.FluidStacksResourceHandler;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;

import java.util.function.BiPredicate;

public class InputFluidHandler extends FluidStacksResourceHandler {

    private final BiPredicate<Integer, FluidStack> canInput;
    private final SyncableBlockEntity blockEntity;
    private boolean internalMode = false;

    public InputFluidHandler(SyncableBlockEntity blockEntity, int size, int capacity,
                             BiPredicate<Integer, FluidStack> canInput) {
        super(size, capacity);
        this.blockEntity = blockEntity;
        this.canInput = canInput;
    }

    @Override
    public boolean isValid(int index, FluidResource resource) {
        if (internalMode) return super.isValid(index, resource);

        FluidStack stack = resource.toStack(1);
        return canInput.test(index, stack) && super.isValid(index, resource);
    }

    @Override
    public int insert(int index, FluidResource resource, int amount, TransactionContext transaction) {
        if (resource.isEmpty()) return 0;

        FluidStack stack = resource.toStack(amount);
        if (!internalMode && !canInput.test(index, stack)) return 0;

        int inserted = super.insert(index, resource, amount, transaction);
        if (inserted > 0) onContentsChanged(index, getStackFrom(resource, inserted));

        return inserted;
    }

    public int extractInternal(int index, FluidResource resource, int amount, TransactionContext transaction) {
        if (resource.isEmpty()) return 0;

        internalMode = true;
        try {
            int extracted = super.extract(index, resource, amount, transaction);
            if (extracted > 0) onContentsChanged(index, getStackFrom(resource, extracted));
            return extracted;
        } finally {
            internalMode = false;
        }
    }

    @Override
    protected void onContentsChanged(int index, FluidStack previousContents) {
        blockEntity.setChanged();
        blockEntity.sync();
    }
}