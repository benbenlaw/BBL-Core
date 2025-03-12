package com.benbenlaw.core.block.brightable;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.TallGrassBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

public class BrightTallGrassBlock extends TallGrassBlock implements IBrightable {

    public static final BooleanProperty LIT = BlockStateProperties.LIT;
    private final BrightDoublePlantBlock COLORED_DOUBLE_PLANT_BLOCK;
    public BrightTallGrassBlock(Properties properties, BrightDoublePlantBlock coloredDoublePlantBlock) {
        super(properties);
        COLORED_DOUBLE_PLANT_BLOCK = coloredDoublePlantBlock;
        this.registerDefaultState(this.stateDefinition.any().setValue(LIT, Boolean.FALSE));
    }

    @Override
    public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
        if (COLORED_DOUBLE_PLANT_BLOCK.defaultBlockState().canSurvive(level, pos) && level.isEmptyBlock(pos.above())) {
            DoublePlantBlock.placeAt(level, COLORED_DOUBLE_PLANT_BLOCK.defaultBlockState(), pos, 2);
        }

    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(LIT);
    }
}