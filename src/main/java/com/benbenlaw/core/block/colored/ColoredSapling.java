package com.benbenlaw.core.block.colored;

import com.benbenlaw.core.block.colored.util.ColorMap;
import com.benbenlaw.core.block.colored.util.IColored;
import com.benbenlaw.core.item.CoreDataComponents;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.grower.TreeGrower;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

public class ColoredSapling extends SaplingBlock implements IColored {

    public static Map<DyeColor, TreeGrower> treeGrowerMap = new HashMap<>();
    public static final EnumProperty<DyeColor> COLOR = EnumProperty.create("color", DyeColor.class);
    public static final BooleanProperty LIT = BlockStateProperties.LIT;
    public static TreeGrower updatedTreeGrower = null;

    public ColoredSapling(TreeGrower treeGrower, Properties properties) {
        super(updatedTreeGrower, properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(STAGE, Integer.valueOf(0)));

    }

    @Override
    public void advanceTree(ServerLevel p_222001_, BlockPos p_222002_, BlockState p_222003_, RandomSource p_222004_) {
        performBonemeal(p_222001_, p_222004_, p_222002_, p_222003_);
    }

    @Override
    public boolean onTreeGrow(BlockState state, LevelReader level, BiConsumer<BlockPos, BlockState> placeFunction, RandomSource randomSource, BlockPos pos, TreeConfiguration config) {
        DyeColor color = state.getValue(COLOR);
        TreeGrower treeGrower = treeGrowerMap.get(color);
        if (treeGrower != null) {
            treeGrower.growTree((ServerLevel) level, ((ServerLevel) level).getChunkSource().getGenerator(), pos, state, randomSource);
            return true;
        }
        return super.onTreeGrow(state, level, placeFunction, randomSource, pos, config);
    }


    @Override
    public void performBonemeal(ServerLevel world, RandomSource random, BlockPos pos, BlockState state) {
        DyeColor color = state.getValue(COLOR);
        TreeGrower treeGrower = treeGrowerMap.get(color);
        updatedTreeGrower = treeGrowerMap.get(color);
        if (treeGrower != null) {
            treeGrower.growTree(world, world.getChunkSource().getGenerator(), pos, state, random);
        }
    }

    public static void updateTreeGrowerMap(DyeColor color, TreeGrower treeGrower) {
        treeGrowerMap.put(color, treeGrower);
    }


    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_55484_) {
        p_55484_.add(LIT, COLOR, STAGE);
    }

    //Coloring
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
        }
        return drops;
    }

    @Override
    public ItemStack getCloneItemStack(BlockState state, HitResult target, LevelReader level, BlockPos pos, Player player) {

        if (state.getBlock() instanceof ColoredSapling) {
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