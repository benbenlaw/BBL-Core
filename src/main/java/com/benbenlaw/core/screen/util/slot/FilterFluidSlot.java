package com.benbenlaw.core.screen.util.slot;

import com.benbenlaw.core.block.entity.handler.fluid.FilterFluidHandler;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.transfer.IndexModifier;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.fluid.FluidUtil;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ResourceHandlerSlot;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import org.jetbrains.annotations.NotNull;

public class FilterFluidSlot extends Slot {

    private final FilterFluidHandler handler;

    public FilterFluidSlot(FilterFluidHandler handler, int index, int xPosition, int yPosition) {
        super(new SimpleContainer(1), index, xPosition, yPosition);
        this.handler = handler;
    }

    @Override
    public boolean mayPlace(@NotNull ItemStack stack) {
        FluidStack fluidStack = FluidUtil.getFirstStackContained(stack);
        return !fluidStack.isEmpty();
    }

    public void set(FluidStack stack, int slot) {
        handler.set(slot, FluidResource.of(stack), 1000);
    }

    public void setEmpty(int slot) {
        handler.set(slot, FluidResource.EMPTY, 0);
    }

    @Override
    public void setChanged() {
        super.setChanged();
    }
}

