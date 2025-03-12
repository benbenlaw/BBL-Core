package com.benbenlaw.core.block.brightable;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

public class BrightFenceGate extends FenceGateBlock implements IBrightable {

    public static final BooleanProperty LIT = BlockStateProperties.LIT;

    public BrightFenceGate(Properties properties, SoundEvent openSound, SoundEvent closeSound) {
        super(properties, openSound, closeSound);
        this.registerDefaultState(this.stateDefinition.any().setValue(LIT, Boolean.FALSE).setValue(OPEN, Boolean.FALSE).setValue(POWERED, Boolean.FALSE).setValue(IN_WALL, Boolean.FALSE));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(LIT, FACING, OPEN, POWERED, IN_WALL);
    }
}