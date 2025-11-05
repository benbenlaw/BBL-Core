package com.benbenlaw.core.multiblock;

import com.mojang.datafixers.util.Pair;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.common.util.ValueIOSerializable;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public final class MultiblockData implements ValueIOSerializable {
    private BlockPos controllerPos;
    private Pair<BlockPos, BlockPos> topCorners;
    private List<BlockPos> multiblockExtraBlocks;
    private int height;
    private int volume;
    private final Set<BlockPos> allBlockPositions;

    public MultiblockData(BlockPos controllerPos, Pair<BlockPos, BlockPos> topCorners, List<BlockPos> extraBlocks, Set<BlockPos> allBlockPositions, int height, int volume) {
        this.controllerPos = controllerPos;
        this.topCorners = topCorners;
        this.multiblockExtraBlocks = extraBlocks;
        this.allBlockPositions = allBlockPositions;
        this.height = height;
        this.volume = volume;
    }

    public BlockPos controllerPos() {
        return controllerPos;
    }

    public Pair<BlockPos, BlockPos> topCorners() {
        return topCorners;
    }

    public List<BlockPos> extraBlocks() {
        return multiblockExtraBlocks;
    }

    public Set<BlockPos> allBlockPositions() {
        return allBlockPositions;
    }

    public int height() {
        return height;
    }

    public int volume() {
        return volume;
    }

    @Override
    public void serialize(ValueOutput valueOutput) {
        valueOutput.putInt("height", height());
        valueOutput.putInt("volume", volume());
        valueOutput.putLong("controller", controllerPos().asLong());
        valueOutput.putLong("corner1", topCorners().getFirst().asLong());
        valueOutput.putLong("corner2", topCorners().getSecond().asLong());
        valueOutput.putInt("extraBlocksCount", extraBlocks().size());
        int i = 0;
        for (BlockPos blockPos : extraBlocks()) {
            valueOutput.putLong("e" + i, blockPos.asLong());
            i++;
        }
    }

    @Override
    public void deserialize(ValueInput valueInput) {
        this.height = valueInput.getIntOr("height", 0);
        this.volume = valueInput.getIntOr("volume", 0);
        this.controllerPos = BlockPos.of(valueInput.getLongOr("controller", 0L));
        this.topCorners = Pair.of(BlockPos.of(valueInput.getLongOr("corner1", 0L)), BlockPos.of(valueInput.getLongOr("corner2", 0L)));
        List<BlockPos> ps = new ArrayList<>();
        for (int i = 0; i < valueInput.getIntOr("extraBlocksCount", 0); i++) {
            ps.add(BlockPos.of(valueInput.getIntOr("e" + i, 0)));
        }
        this.multiblockExtraBlocks = ps;

    }
}