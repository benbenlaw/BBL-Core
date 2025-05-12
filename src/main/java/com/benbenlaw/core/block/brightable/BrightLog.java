package com.benbenlaw.core.block.brightable;

import net.minecraft.core.Direction;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.neoforged.neoforge.common.ItemAbility;
import org.jetbrains.annotations.Nullable;

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
    public @Nullable BlockState getToolModifiedState(BlockState state, UseOnContext context, ItemAbility itemAbility, boolean simulate) {
        if (context.getItemInHand().getItem() instanceof AxeItem) {

            Block block = state.getBlock();

            if (logStrippedMap.containsKey(block)) {

                return logStrippedMap.get(block).defaultBlockState()
                        .setValue(AXIS, state.getValue(AXIS));
            } else if (woodStrippedMap.containsKey(block)) {

                return woodStrippedMap.get(block).defaultBlockState()
                        .setValue(AXIS, state.getValue(AXIS));
            }
        }
        return super.getToolModifiedState(state, context, itemAbility, simulate);
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