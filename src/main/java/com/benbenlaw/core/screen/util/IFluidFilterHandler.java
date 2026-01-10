package com.benbenlaw.core.screen.util;

import net.minecraft.world.inventory.AbstractContainerMenu;
import net.neoforged.neoforge.fluids.FluidStack;

public interface IFluidFilterHandler {
    void setFilter(int slot, FluidStack stack);
    void clearFilter(int slot);
    FluidStack getFilter(int slot);
}