package com.benbenlaw.core.block.entity.handler.item;

import com.benbenlaw.core.block.entity.SyncableBlockEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ItemStacksResourceHandler;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;

import java.util.function.Predicate;

@Deprecated(since = "12.5.0", forRemoval = true)
public class OutputItemHandler extends ItemStacksResourceHandler {

    private final Predicate<Integer> canExtract;
    private final SyncableBlockEntity blockEntity;
    private boolean internalMode = false; // allow internal insertion

    public OutputItemHandler(SyncableBlockEntity blockEntity, int size, Predicate<Integer> canExtract) {
        super(size);
        this.canExtract = canExtract;
        this.blockEntity = blockEntity;
    }

    @Override
    public int insert(int index, ItemResource resource, int amount, TransactionContext transaction) {
        if (internalMode) {
            return super.insert(index, resource, amount, transaction);
        }
        return 0; // block external insertion
    }

    /** Allows machine logic to insert items internally */
    public void insertInternal(int index, ItemResource resource, int amount, TransactionContext transaction) {
        if (resource.isEmpty()) return;
        internalMode = true;
        try {
            super.insert(index, resource, amount, transaction);
        } finally {
            internalMode = false;
        }
    }

    /** Allows machine logic to insert items internally and get the amount inserted */
    public int insertInternalReturn(int index, ItemResource resource, int amount, TransactionContext transaction) {
        if (resource.isEmpty()) return 0;
        this.internalMode = true;
        try {
            return super.insert(index, resource, amount, transaction);
        } finally {
            this.internalMode = false;
        }
    }

    @Override
    public boolean isValid(int index, ItemResource resource) {
        return internalMode; // only valid during internal insertion
    }

    @Override
    public int extract(int index, ItemResource resource, int amount, TransactionContext transaction) {
        if (resource.isEmpty() || !this.canExtract.test(index)) {
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
