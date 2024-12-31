package com.benbenlaw.core.block.colored;

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

public class ColoredFlowerPot extends FlowerPotBlock implements IColored {

    public static final EnumProperty<DyeColor> COLOR = EnumProperty.create("color", DyeColor.class);
    public static final BooleanProperty LIT = BlockStateProperties.LIT;

    public ColoredFlowerPot(@Nullable Supplier<FlowerPotBlock> emptyPot, Supplier<? extends Block> flower, Properties properties) {
        super(emptyPot, flower, properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(COLOR, DyeColor.WHITE).setValue(LIT, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(COLOR, LIT);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState blockState, Level level, BlockPos blockPos, Player player, BlockHitResult hitResult) {

        ItemStack itemstack = new ItemStack(this.getPotted());
        DyeColor color = blockState.getValue(COLOR);
        itemstack.set(CoreDataComponents.COLOR, color.toString());
        itemstack.set(CoreDataComponents.LIT, blockState.getValue(LIT));
        player.addItem(itemstack);
        level.setBlock(blockPos, getEmptyPot().defaultBlockState(), 3);
        level.gameEvent(player, GameEvent.BLOCK_CHANGE, blockPos);
        return InteractionResult.sidedSuccess(level.isClientSide);

    }

    @Override
    public int getColor(int index) {
        DyeColor color = DyeColor.values()[index % DyeColor.values().length];
        return ColorMap.getColorValue(color);
    }

    @Override
    public int getColor(int index, ItemStack stack) {
        return getColor(index);
    }


    @Override
    public @NotNull List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
        List<ItemStack> drops = super.getDrops(state, builder);
        DyeColor color = state.getValue(COLOR);

        for (ItemStack drop : drops) {
            if (drop.getItem() instanceof BlockItem && ((BlockItem) drop.getItem()).getBlock() == this) {
                drop.set(CoreDataComponents.COLOR, color.toString());
                drop.set(CoreDataComponents.LIT, state.getValue(LIT));
            }
            if (Block.byItem(drop.getItem()) instanceof ColoredFlower) {
                drop.set(CoreDataComponents.COLOR, color.toString());
                drop.set(CoreDataComponents.LIT, state.getValue(LIT));
            }
        }
        return drops;
    }

    @Override
    public ItemStack getCloneItemStack(BlockState state, HitResult target, LevelReader level, BlockPos pos, Player player) {

        if (state.getBlock() instanceof ColoredFlowerPot) {
            DyeColor color = state.getValue(COLOR);
            ItemStack stack = new ItemStack(this);
            stack.set(CoreDataComponents.COLOR, color.toString());
            stack.set(CoreDataComponents.LIT, state.getValue(LIT));
            return stack;
        }

        return super.getCloneItemStack(state, target, level, pos, player);
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(level, pos, state, placer, stack);

        if (stack.get(CoreDataComponents.COLOR) != null && stack.get(CoreDataComponents.LIT) != null) {
            String colorString = stack.get(CoreDataComponents.COLOR);
            assert colorString != null;
            DyeColor dyeColor = ColorMap.getDyeColor(colorString);

            Boolean lit = stack.get(CoreDataComponents.LIT);
            assert lit != null;

            BlockState newState = state.setValue(COLOR, dyeColor).setValue(LIT, lit);

            level.setBlockAndUpdate(pos, newState);
        } else {
            level.setBlockAndUpdate(pos, state);
        }
    }





}