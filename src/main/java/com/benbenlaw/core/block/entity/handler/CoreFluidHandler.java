package com.benbenlaw.core.block.entity.handler;

import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import org.jetbrains.annotations.NotNull;

public class CoreFluidHandler implements IFluidHandler {

    /// Use an array or single tank to provide simple fluid handling for block entities
    private final FluidTank[] tanks;

    public CoreFluidHandler(FluidTank... tanks) {
        this.tanks = tanks;
    }

    @Override
    public int getTanks() {
        return tanks.length;
    }

    @Override
    public @NotNull FluidStack getFluidInTank(int tank) {
        if (tank < 0 || tank >= tanks.length) return FluidStack.EMPTY;
        return tanks[tank].getFluid();
    }

    @Override
    public int getTankCapacity(int tank) {
        if (tank < 0 || tank >= tanks.length) return 0;
        return tanks[tank].getCapacity();
    }

    @Override
    public boolean isFluidValid(int tank, @NotNull FluidStack stack) {
        if (tank < 0 || tank >= tanks.length) return false;
        return tanks[tank].isFluidValid(stack);
    }

    @Override
    public int fill(@NotNull FluidStack resource, @NotNull FluidAction action) {
        for (FluidTank tank : tanks) {
            int filled = tank.fill(resource, action);
            if (filled > 0) return filled;
        }
        return 0;
    }

    @Override
    public @NotNull FluidStack drain(@NotNull FluidStack resource, @NotNull FluidAction action) {
        for (FluidTank tank : tanks) {
            FluidStack drained = tank.drain(resource, action);
            if (!drained.isEmpty()) return drained;
        }
        return FluidStack.EMPTY;
    }

    @Override
    public @NotNull FluidStack drain(int maxDrain, @NotNull FluidAction action) {
        for (FluidTank tank : tanks) {
            FluidStack drained = tank.drain(maxDrain, action);
            if (!drained.isEmpty()) return drained;
        }
        return FluidStack.EMPTY;
    }
}