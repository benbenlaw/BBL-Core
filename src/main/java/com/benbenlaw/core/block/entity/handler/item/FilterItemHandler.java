package com.benbenlaw.core.block.entity.handler.item;

import com.benbenlaw.core.block.entity.SyncableBlockEntity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ItemStacksResourceHandler;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;

import java.util.function.BiPredicate;

public class FilterItemHandler extends ItemStacksResourceHandler {

    private final SyncableBlockEntity blockEntity;

    public FilterItemHandler(SyncableBlockEntity blockEntity, int size) {
        super(size);
        this.blockEntity = blockEntity;
    }

    @Override
    public boolean isValid(int index, ItemResource resource) {
        return false;
    }

    public boolean isBlockItem(ItemResource resource) {
        if (resource == null || resource.getItem() == null) return false;
        return resource.getItem() instanceof BlockItem;
    }

    public Block getBlockFromItem(ItemResource resource) {
        if (!isBlockItem(resource)) return null;
        return ((BlockItem) resource.getItem()).getBlock();
    }

    public boolean matchesBlockState(ItemResource resource, BlockState blockState) {
        Block blockFromItem = getBlockFromItem(resource);
        if (blockFromItem == null) return false;
        return blockFromItem == blockState.getBlock();
    }

    public boolean matchesBlockState(BlockState blockState, boolean whitelist) {
        boolean hasAnyFilter = false;

        for (int i = 0; i < this.size(); i++) {
            ItemResource resource = this.getResource(i);

            hasAnyFilter = true;

            if (matchesBlockState(resource, blockState)) {

                return whitelist;
            }
        }

        return !hasAnyFilter || !whitelist;
    }

    @Override
    public int insert(int index, ItemResource resource, int amount, TransactionContext transaction) {
        return 0;
    }

    @Override
    public int extract(int index, ItemResource resource, int amount, TransactionContext transaction) {
        return 0;
    }

    @Override
    protected void onContentsChanged(int index, ItemStack previousContents) {
        blockEntity.setChanged();
        blockEntity.sync();
    }
}