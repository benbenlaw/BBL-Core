package com.benbenlaw.core.block.brightable;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

import java.util.HashMap;
import java.util.Map;
//TODO implement axe logic
public class BrightLog extends RotatedPillarBlock implements IBrightable {

    public static Map<Block, Block> logStrippedMap = new HashMap<>();
    public static Map<Block, Block> woodStrippedMap = new HashMap<>();

    public static final BooleanProperty LIT = BlockStateProperties.LIT;
    public BrightLog(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(LIT, Boolean.FALSE).setValue(AXIS, Direction.Axis.Y));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(LIT, AXIS);
    }

    public static void updateLogStrippedMap(Block original, Block stripped) {
        logStrippedMap.put(original, stripped);
    }

    public static void updateWoodStrippedMap(Block original, Block stripped) {
        woodStrippedMap.put(original, stripped);
    }
}