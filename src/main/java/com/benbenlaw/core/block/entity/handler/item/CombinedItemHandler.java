package com.benbenlaw.core.block.entity.handler.item;

import com.benbenlaw.core.block.entity.handler.fluid.OutputItemHandler;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;

public class CombinedItemHandler implements ResourceHandler<ItemResource> {

    private final InputItemHandler inputHandler;
    private final OutputItemHandler outputHandler;

    public CombinedItemHandler(InputItemHandler inputHandler, OutputItemHandler outputHandler) {
        this.inputHandler = inputHandler;
        this.outputHandler = outputHandler;
    }

    @Override
    public int size() {
        return inputHandler.size() + outputHandler.size();
    }

    @Override
    public ItemResource getResource(int index) {
        int inputSlots = inputHandler.size();
        if (index < inputSlots) {
            return inputHandler.getResource(index);
        } else {
            return outputHandler.getResource(index - inputSlots);
        }
    }

    @Override
    public long getAmountAsLong(int index) {
        int inputSlots = inputHandler.size();
        if (index < inputSlots) {
            return inputHandler.getAmountAsLong(index);
        } else {
            return outputHandler.getAmountAsLong(index - inputSlots);
        }
    }

    @Override
    public long getCapacityAsLong(int index, ItemResource resource) {
        int inputSlots = inputHandler.size();
        if (index < inputSlots) {
            return inputHandler.getCapacityAsLong(index, resource);
        } else {
            return outputHandler.getCapacityAsLong(index - inputSlots, resource);
        }
    }

    @Override
    public boolean isValid(int index, ItemResource resource) {
        int inputSlots = inputHandler.size();
        if (index < inputSlots) {
            return inputHandler.isValid(index, resource);
        } else {
            return outputHandler.isValid(index - inputSlots, resource);
        }
    }

    @Override
    public int insert(int index, ItemResource resource, int amount, TransactionContext transaction) {
        int inputSlots = inputHandler.size();
        if (index < inputSlots) {
            return inputHandler.insert(index, resource, amount, transaction);
        } else {
            return 0;
        }
    }

    @Override
    public int extract(int index, ItemResource resource, int amount, TransactionContext transaction) {
        int inputSlots = inputHandler.size();
        if (index < inputSlots) {
            return 0;
        } else {
            return outputHandler.extract(index - inputSlots, resource, amount, transaction);
        }
    }
}
