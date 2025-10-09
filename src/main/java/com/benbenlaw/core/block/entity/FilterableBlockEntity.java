package com.benbenlaw.core.block.entity;

public interface FilterableBlockEntity {

    FilterItemHandler getFilterItemHandler();

    boolean isWhitelist();

    void setWhitelist(boolean whitelist);
}
