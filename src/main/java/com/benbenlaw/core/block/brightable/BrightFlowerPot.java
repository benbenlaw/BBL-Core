package com.benbenlaw.core.block.brightable;

import com.benbenlaw.core.block.colored.ColoredFlower;
import com.benbenlaw.core.block.colored.util.ColorMap;
import com.benbenlaw.core.block.colored.util.IColored;
import com.benbenlaw.core.item.CoreDataComponents;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Supplier;

public class BrightFlowerPot extends FlowerPotBlock
{
    public static final BooleanProperty LIT = BlockStateProperties.LIT;

    public BrightFlowerPot(@Nullable Supplier<FlowerPotBlock> emptyPot, Supplier<? extends Block> flower, Properties properties) {
        super(emptyPot, flower, properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(LIT, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(LIT);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState blockState, Level level, BlockPos blockPos, Player player, BlockHitResult hitResult) {

        //TODO implement this
        //ItemStack itemstack = new ItemStack(this.getPotted());
        //DyeColor color = blockState.getValue(COLOR);
        //itemstack.set(CoreDataComponents.COLOR, color.toString());
        //itemstack.set(CoreDataComponents.LIT, blockState.getValue(LIT));
        //player.addItem(itemstack);
        //level.setBlock(blockPos, getEmptyPot().defaultBlockState(), 3);
        //level.gameEvent(player, GameEvent.BLOCK_CHANGE, blockPos);
        //return InteractionResult.sidedSuccess(level.isClientSide);

        return InteractionResult.sidedSuccess(level.isClientSide);
    }
}