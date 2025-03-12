package com.benbenlaw.core.block.brightable;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.grower.TreeGrower;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

public class BrightSapling extends SaplingBlock implements IBrightable {

    public static final BooleanProperty LIT = BlockStateProperties.LIT;
    private final TreeGrower TREE_GROWER;


    public BrightSapling(TreeGrower treeGrower, Properties properties) {
        super(treeGrower, properties);
        this.TREE_GROWER = treeGrower;
        this.registerDefaultState(this.stateDefinition.any().setValue(STAGE, 0).setValue(LIT, false));
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_55484_) {
        p_55484_.add(LIT, STAGE);
    }

    @Override
    public void advanceTree(ServerLevel level, BlockPos pos, BlockState state, RandomSource random) {
        if (state.getValue(STAGE) == 0) {
            level.setBlock(pos, state.cycle(STAGE), 4);
        } else {
            this.TREE_GROWER.growTree(level, level.getChunkSource().getGenerator(), pos, state, random);
        }
    }
}