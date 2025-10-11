package com.benbenlaw.core.block.entity;

public interface WhitelistBlockEntity {
    /**
     * @return true if this block entity is in whitelist mode,
     *         false if it’s in blacklist mode.
     */
    boolean isWhitelist();

    /**
     * Sets the whitelist mode.
     *
     * @param whitelist true for whitelist, false for blacklist
     */
    void setWhitelist(boolean whitelist);

    /**
     * Toggles the whitelist/blacklist mode.
     * (Optional convenience method)
     */
    default void toggleWhitelist() {
        setWhitelist(!isWhitelist());
    }
}
