package com.benbenlaw.core.block.entity;

import com.benbenlaw.core.block.entity.handler.FilterItemHandler;

public interface FilterableBlockEntity {

    FilterItemHandler getFilterItemHandler();

    boolean isWhitelist();

    void setWhitelist(boolean whitelist);
}
