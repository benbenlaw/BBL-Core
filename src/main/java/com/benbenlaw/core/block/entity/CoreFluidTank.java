package com.benbenlaw.core.block.entity;

import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;

public class CoreFluidTank extends FluidTank {

    /// Used to create a simple fluid tank inside a block entity that syncs its contents when changed
    SyncableBlockEntity blockEntity;
    String tankName;

    public CoreFluidTank(SyncableBlockEntity blockEntity, int capacity, String tankName) {
        super(capacity);
        this.blockEntity = blockEntity;
        this.tankName = tankName;
    }

    @Override
    protected void onContentsChanged() {
        blockEntity.setChanged();
        blockEntity.sync();
    }

    @Override
    public void serialize(ValueOutput output) {
        if (!this.fluid.isEmpty()) {
            output.store(tankName, FluidStack.CODEC, this.fluid);
        }
    }

    @Override
    public void deserialize(ValueInput input) {
        this.fluid = input.read(tankName, FluidStack.CODEC).orElse(FluidStack.EMPTY);
    }
}
