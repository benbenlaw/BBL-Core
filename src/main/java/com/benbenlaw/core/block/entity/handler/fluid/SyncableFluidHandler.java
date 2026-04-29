package com.benbenlaw.core.block.entity.handler.fluid;

import com.benbenlaw.core.block.entity.SyncableBlockEntity;
import com.benbenlaw.core.block.entity.handler.item.SyncableItemHandler;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.transfer.fluid.FluidStacksResourceHandler;

public class SyncableFluidHandler extends FluidStacksResourceHandler {

    SyncableBlockEntity blockEntity;

    public SyncableFluidHandler(SyncableBlockEntity blockEntity, int size, int capacity) {
        super(size, capacity);
        this.blockEntity = blockEntity;
    }

    @Override
    protected void onContentsChanged(int index, FluidStack previousContents) {
        super.onContentsChanged(index, previousContents);
        blockEntity.setChanged();
        blockEntity.sync();
    }
}
