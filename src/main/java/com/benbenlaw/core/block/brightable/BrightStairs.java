package com.benbenlaw.core.block.brightable;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

public class BrightStairs extends StairBlock implements IBrightable {

    public static final BooleanProperty LIT = BlockStateProperties.LIT;
    public BrightStairs(BlockState blockState, Properties properties) {
        super(blockState, properties);
        this.registerDefaultState(this.defaultBlockState().setValue(LIT, Boolean.FALSE));

    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_55484_) {
        p_55484_.add(LIT, FACING, HALF, SHAPE, WATERLOGGED);
    }
}