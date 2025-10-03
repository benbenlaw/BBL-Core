package com.benbenlaw.core.block.entity.handler;

import com.benbenlaw.core.block.entity.SyncableBlockEntity;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.fluid.FluidStacksResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ItemStacksResourceHandler;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;

import java.util.function.BiPredicate;
import java.util.function.Predicate;

public class InputOutputFluidHandler extends FluidStacksResourceHandler {
    private final BiPredicate<Integer, FluidStack> canInput;
    private final Predicate<Integer> canOutput;
    private final SyncableBlockEntity blockEntity;

    public InputOutputFluidHandler(SyncableBlockEntity blockEntity, int size, int capacity, BiPredicate<Integer, FluidStack> canInput, Predicate<Integer> canOutput) {
        super(size, capacity);
        this.canInput = canInput;
        this.canOutput = canOutput;
        this.blockEntity = blockEntity;
    }

    @Override
    public boolean isValid(int index, FluidResource resource) {
        FluidStack stack = resource.toStack(1);
        return canInput.test(index, stack) && super.isValid(index, resource);
    }

    @Override
    public int insert(int index, FluidResource resource, int amount, TransactionContext transaction) {
        FluidStack stack = resource.toStack(amount);
        if (!canInput.test(index, stack)) return 0;
        int inserted = super.insert(index, resource, amount, transaction);
        if (inserted > 0) onContentsChanged(index, getStackFrom(resource, inserted));
        return inserted;
    }

    @Override
    public int extract(int index, FluidResource resource, int amount, TransactionContext transaction) {
        if (!canOutput.test(index)) return 0;
        int extracted = super.extract(index, resource, amount, transaction);
        if (extracted > 0) onContentsChanged(index, getStackFrom(resource, extracted));
        return extracted;
    }

    @Override
    protected void onContentsChanged(int index, FluidStack previousContents) {
        blockEntity.setChanged();
        blockEntity.sync();
    }
}
