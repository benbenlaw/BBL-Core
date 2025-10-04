package com.benbenlaw.core.block.entity.handler;

import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.transaction.Transaction;

import java.util.function.Consumer;

public class TransactionHelper {

    public static void withTransaction(Consumer<Transaction> action) {
        try (Transaction tx = Transaction.open(null)) {
            action.accept(tx);
            tx.commit();
        }
    }

    public static void extract(InputOutputItemHandler handler, int slot, ItemResource resource) {
        withTransaction(tx -> handler.extract(slot, resource, 1, tx));
    }

    public static void insert(InputOutputItemHandler handler, int slot, ItemResource resource) {
        withTransaction(tx -> handler.insert(slot, resource, 1, tx));
    }

    public static void extract(InputOutputFluidHandler handler, int slot, FluidResource resource, int amount, Transaction tx) {
        handler.extract(slot, resource, amount, tx);
    }

    public static void insert(InputOutputFluidHandler handler, int slot, FluidResource resource, int amount, Transaction tx) {
        handler.insert(slot, resource, amount, tx);
    }
}