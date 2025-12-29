package com.benbenlaw.core.block.entity.handler.fluid;

import com.benbenlaw.core.block.entity.SyncableBlockEntity;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.fluid.FluidStacksResourceHandler;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;

import java.util.function.BiPredicate;

public class FilterFluidHandler extends FluidStacksResourceHandler {

    private final SyncableBlockEntity blockEntity;
    public FilterFluidHandler(SyncableBlockEntity blockEntity, int size) {
        super(size, 1000);
        this.blockEntity = blockEntity;
    }

    public boolean matchesFluid(FluidResource resource, FluidStack stack) {
        if (resource == null || stack == null) return false;
        return resource.matches(stack);
    }

    //Use this to compare a FluidStack to the filter contents with whitelist/blacklist functionality
    public boolean matchesFluid(FluidStack stack, boolean whitelist) {
        boolean hasAnyFilter = false;

        for (int i = 0; i < size(); i++) {
            FluidResource resource = getResource(i);

            if (resource == null || resource.isEmpty()) {
                continue;
            }

            hasAnyFilter = true;

            if (resource.getFluid().isSame(stack.getFluid())) {
                return whitelist;
            }
        }

        if (!hasAnyFilter) {
            return true;
        }

        return !whitelist;
    }


    @Override
    protected void onContentsChanged(int index, FluidStack previousContents) {
        blockEntity.setChanged();
        blockEntity.sync();
    }
}