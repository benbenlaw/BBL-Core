package com.benbenlaw.core.block.entity.handler.fluid;

import com.benbenlaw.core.block.entity.SyncableBlockEntity;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.fluid.FluidStacksResourceHandler;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;

import java.util.function.Predicate;

public class OutputFluidHandler extends FluidStacksResourceHandler {

    private final Predicate<Integer> canOutput;
    private final SyncableBlockEntity blockEntity;
    private boolean internalMode = false;

    public OutputFluidHandler(SyncableBlockEntity blockEntity, int size, int capacity,
                              Predicate<Integer> canOutput) {
        super(size, capacity);
        this.blockEntity = blockEntity;
        this.canOutput = canOutput;
    }

    @Override
    public int extract(int index, FluidResource resource, int amount, TransactionContext transaction) {
        if (resource.isEmpty()) return 0;
        if (!internalMode && !canOutput.test(index)) return 0;

        int extracted = super.extract(index, resource, amount, transaction);
        if (extracted > 0) onContentsChanged(index, getStackFrom(resource, extracted));

        return extracted;
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