package com.benbenlaw.core.block.entity.handler;

import com.benbenlaw.core.block.entity.SyncableBlockEntity;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ItemStacksResourceHandler;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;

import java.util.function.BiPredicate;
import java.util.function.Predicate;

public class InputOutputItemHandler extends ItemStacksResourceHandler {
    private final BiPredicate<Integer, ItemStack> canInput;
    private final Predicate<Integer> canOutput;
    private final SyncableBlockEntity blockEntity;

    public InputOutputItemHandler(SyncableBlockEntity blockEntity, int size, BiPredicate<Integer, ItemStack> canInput, Predicate<Integer> canOutput) {
        super(size);
        this.canInput = canInput;
        this.canOutput = canOutput;
        this.blockEntity = blockEntity;
    }

    @Override
    public boolean isValid(int index, ItemResource resource) {
        ItemStack stack = resource.toStack(1);
        return canInput.test(index, stack) && super.isValid(index, resource);
    }

    @Override
    public int insert(int index, ItemResource resource, int amount, TransactionContext transaction) {
        ItemStack stack = resource.toStack(amount);
        if (!canInput.test(index, stack)) {
            return 0;
        }
        return super.insert(index, resource, amount, transaction);
    }

    @Override
    public int extract(int index, ItemResource resource, int amount, TransactionContext transaction) {
        if (!canOutput.test(index)) {
            return 0;
        }
        return super.extract(index, resource, amount, transaction);
    }

    @Override
    protected void onContentsChanged(int index, ItemStack previousContents) {
        blockEntity.setChanged();
        blockEntity.sync();
    }
}
