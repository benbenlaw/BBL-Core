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

@Deprecated(since = "12.5.0", forRemoval = true)
public class FilterItemHandler extends ItemStacksResourceHandler {

    private final SyncableBlockEntity blockEntity;

    public FilterItemHandler(SyncableBlockEntity blockEntity, int size) {
        super(size);
        this.blockEntity = blockEntity;
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

    //Use this to compare a BlockState to the filter contents item as a block item with whitelist/blacklist functionality
    public boolean matchesBlockState(BlockState blockState, boolean whitelist) {
        boolean hasAnyFilter = false;

        for (int i = 0; i < this.size(); i++) {
            ItemResource resource = this.getResource(i);

            if (resource == null || resource.isEmpty()) {
                continue;
            }

            hasAnyFilter = true;

            if (matchesBlockState(resource, blockState)) {
                return whitelist;
            }
        }

        if (!hasAnyFilter) {
            return true;
        }

        return !whitelist;
    }


    public boolean matchesItem(ItemResource resource, ItemStack stack) {
        if (resource == null || stack == null) return false;
        return ItemStack.isSameItemSameComponents(resource.toStack(), stack);
    }

    //Use this to compare an ItemStack to the filter contents item with whitelist/blacklist functionality, checks itemstack not item
    public boolean matchesItem(ItemStack stack, boolean whitelist) {
        boolean hasAnyFilter = false;

        for (int i = 0; i < this.size(); i++) {
            ItemResource resource = this.getResource(i);

            if (resource == null || resource.isEmpty()) {
                continue;
            }

            hasAnyFilter = true;

            if (matchesItem(resource, stack)) {
                return whitelist;
            }
        }

        // No filters → allow everything
        if (!hasAnyFilter) {
            return true;
        }

        return !whitelist;
    }



    public boolean matchesItem(ItemResource resource, boolean whitelist, boolean ignoreNbt) {
        boolean foundMatch = false;

        for (int i = 0; i < size(); i++) {
            ItemStack filterStack = getResource(i).toStack();
            if (filterStack.isEmpty()) continue;

            boolean matches = ignoreNbt
                    ? resource.getItem() == filterStack.getItem()
                    : ItemStack.isSameItemSameComponents(resource.toStack(), filterStack);

            if (matches) {
                foundMatch = true;
                break;
            }
        }

        // Apply whitelist / blacklist AFTER matching
        return whitelist ? foundMatch : !foundMatch;
    }


    @Override
    protected void onContentsChanged(int index, ItemStack previousContents) {
        blockEntity.setChanged();
        blockEntity.sync();
    }
}