package com.benbenlaw.core.block.colored.util;

import com.benbenlaw.core.block.brightable.BrightBlock;
import com.benbenlaw.core.block.colored.*;
import com.benbenlaw.core.block.colored.flammable.*;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.grower.TreeGrower;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class BlockTypeColorFinder {

    public static final HashMap<Class<? extends Block>, Function<BlockState, DyeColor>> BLOCK_TYPE_COLOR_FINDER = new HashMap<>() {{

        put(ColoredBlock.class, blockState -> blockState.getValue(ColoredBlock.COLOR));
        put(ColoredStairs.class, blockState -> blockState.getValue(ColoredStairs.COLOR));
        put(ColoredSlab.class, blockState -> blockState.getValue(ColoredSlab.COLOR));
        put(ColoredWall.class, blockState -> blockState.getValue(ColoredWall.COLOR));
        put(ColoredPressurePlate.class, blockState -> blockState.getValue(ColoredPressurePlate.COLOR));
        put(ColoredButton.class, blockState -> blockState.getValue(ColoredButton.COLOR));
        put(ColoredFence.class, blockState -> blockState.getValue(ColoredFence.COLOR));
        put(ColoredFenceGate.class, blockState -> blockState.getValue(ColoredFenceGate.COLOR));
        put(ColoredDoor.class, blockState -> blockState.getValue(ColoredDoor.COLOR));
        put(ColoredTrapDoor.class, blockState -> blockState.getValue(ColoredTrapDoor.COLOR));
        put(ColoredLog.class, blockState -> blockState.getValue(ColoredLog.COLOR));
        put(ColoredLeaves.class, blockState -> blockState.getValue(ColoredLeaves.COLOR));
        put(ColoredSapling.class, blockState -> blockState.getValue(ColoredSapling.COLOR));
        put(ColoredFlower.class, blockState -> blockState.getValue(ColoredFlower.COLOR));
        put(ColoredFlowerPot.class, blockState -> blockState.getValue(ColoredFlowerPot.COLOR));
        put(ColoredCraftingTable.class, blockState -> blockState.getValue(ColoredCraftingTable.COLOR));
        put(ColoredSnowyDirtBlock.class, blockState -> blockState.getValue(ColoredSnowyDirtBlock.COLOR));
        put(ColoredSpreadingSnowyDirtBlock.class, blockState -> blockState.getValue(ColoredSpreadingSnowyDirtBlock.COLOR));
        put(ColoredGrassBlock.class, blockState -> blockState.getValue(ColoredGrassBlock.COLOR));
        put(ColoredTallGrassBlock.class, blockState -> blockState.getValue(ColoredTallGrassBlock.COLOR));
        put(ColoredDoublePlantBlock.class, blockState -> blockState.getValue(ColoredDoublePlantBlock.COLOR));

        put(FlammableColoredLog.class, blockState -> blockState.getValue(FlammableColoredLog.COLOR));
        put(FlammableColoredBlock.class, blockState -> blockState.getValue(FlammableColoredBlock.COLOR));
        put(FlammableColoredFence.class, blockState -> blockState.getValue(FlammableColoredFence.COLOR));
        put(FlammableColoredFenceGate.class, blockState -> blockState.getValue(FlammableColoredFenceGate.COLOR));
        put(FlammableColoredStairs.class, blockState -> blockState.getValue(FlammableColoredStairs.COLOR));
        put(FlammableColoredSlabs.class, blockState -> blockState.getValue(FlammableColoredSlabs.COLOR));
        put(FlammableColoredLeaves.class, blockState -> blockState.getValue(FlammableColoredLeaves.COLOR));


    }};

    public static void updateBlockTypeFinder(Class<? extends Block> blockClass, Function<BlockState, DyeColor> colorFunction) {
        BLOCK_TYPE_COLOR_FINDER.put(blockClass, colorFunction);
    }
}
