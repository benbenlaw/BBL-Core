package com.benbenlaw.core.block.entity.handler.fluid;

import com.benbenlaw.core.block.entity.SyncableBlockEntity;
import com.benbenlaw.core.screen.util.IFluidFilterHandler;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.fluid.FluidStacksResourceHandler;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;

import java.util.function.BiPredicate;

public class FilterFluidHandler extends FluidStacksResourceHandler implements IFluidFilterHandler {

    private final SyncableBlockEntity blockEntity;

    public FilterFluidHandler(SyncableBlockEntity blockEntity, int size) {
        super(size, 1000);
        this.blockEntity = blockEntity;
    }

    @Override
    public void setFilter(int slot, FluidStack stack) {
        this.set(slot, FluidResource.of(stack), 1000);
    }

    @Override
    public void clearFilter(int slot) {
        this.set(slot, FluidResource.EMPTY, 0);
    }

    @Override
    public FluidStack getFilter(int slot) {
        return this.getResource(slot).toStack(1000);
    }

    @Override
    protected void onContentsChanged(int index, FluidStack previousContents) {
        blockEntity.setChanged();
        blockEntity.sync();
    }

    // Existing matching logic still works
    public boolean matchesFluid(FluidResource resource, FluidStack stack) {
        if (resource == null || stack == null) return false;
        return resource.matches(stack);
    }

    public boolean matchesFluid(FluidStack stack, boolean whitelist) {
        boolean hasAnyFilter = false;

        for (int i = 0; i < size(); i++) {
            FluidResource resource = getResource(i);

            if (resource == null || resource.isEmpty()) continue;

            hasAnyFilter = true;

            if (resource.getFluid().isSame(stack.getFluid())) {
                return whitelist;
            }
        }

        return !hasAnyFilter || !whitelist;
    }
}
