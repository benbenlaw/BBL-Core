package com.benbenlaw.core.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Containers;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.storage.ValueInput;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.transfer.item.ItemStacksResourceHandler;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * Provides synchronization capabilities for block entities between server and client.
 * Also adds helper to drop item handler contents when the block is removed.
 */
public class SyncableBlockEntity extends BlockEntity {

    public SyncableBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public void sync() {
        if (level instanceof ServerLevel serverLevel) {
            LevelChunk chunk = serverLevel.getChunkAt(getBlockPos());
            if (Objects.requireNonNull(chunk.getLevel()).getChunkSource() instanceof ServerChunkCache chunkCache) {
                chunkCache.chunkMap.getPlayers(chunk.getPos(), false).forEach(this::syncContents);
            }
        }
    }

    @Override
    public void handleUpdateTag(@NotNull ValueInput input) {
        loadAdditional(input);
    }

    public void syncContents(ServerPlayer player) {
        player.connection.send(Objects.requireNonNull(getUpdatePacket()));
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public @NotNull CompoundTag getUpdateTag(HolderLookup.@NotNull Provider provider) {
        return saveWithoutMetadata(provider);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        this.setChanged();
        this.sync();
    }

    @Override
    public void onDataPacket(@NotNull Connection net, @NotNull ValueInput valueInput) {
        super.onDataPacket(net, valueInput);
        loadAdditional(valueInput);
    }

    protected void dropInventoryContents(ItemStacksResourceHandler handler) {
        NonNullList<ItemStack> stacks = NonNullList.create();
        for (int i = 0; i < handler.size(); i++) {
            stacks.add(handler.getResource(i).toStack());
        }
        if (this.level != null) {
            Containers.dropContents(this.level, this.worldPosition, stacks);
        }
    }
}
