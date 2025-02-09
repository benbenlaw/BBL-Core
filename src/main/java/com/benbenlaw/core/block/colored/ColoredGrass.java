package com.benbenlaw.core.block.colored;

import com.benbenlaw.core.block.colored.util.ColorMap;
import com.benbenlaw.core.block.colored.util.IColored;
import com.benbenlaw.core.item.CoreDataComponents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.placement.VegetationPlacements;
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
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class ColoredGrass extends ColoredSpreadingSnowyDirtBlock implements IColored, BonemealableBlock {

    public static final EnumProperty<DyeColor> COLOR = EnumProperty.create("color", DyeColor.class);
    public static final BooleanProperty LIT = BlockStateProperties.LIT;
    public ColoredGrass(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(COLOR, DyeColor.WHITE).setValue(LIT, Boolean.FALSE));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(COLOR, LIT);
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
        }
        return drops;
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader p_256229_, BlockPos p_256432_, BlockState p_255677_) {
        return p_256229_.getBlockState(p_256432_.above()).isAir();
    }

    @Override
    public boolean isBonemealSuccess(Level p_221275_, RandomSource p_221276_, BlockPos p_221277_, BlockState p_221278_) {
        return true;
    }

    @Override
    public void performBonemeal(ServerLevel p_221270_, RandomSource p_221271_, BlockPos p_221272_, BlockState p_221273_) {
        BlockPos blockpos = p_221272_.above();
        BlockState blockstate = Blocks.SHORT_GRASS.defaultBlockState();
        Optional<Holder.Reference<PlacedFeature>> optional = p_221270_.registryAccess()
                .registryOrThrow(Registries.PLACED_FEATURE)
                .getHolder(VegetationPlacements.GRASS_BONEMEAL);

        label49:
        for (int i = 0; i < 128; i++) {
            BlockPos blockpos1 = blockpos;

            for (int j = 0; j < i / 16; j++) {
                blockpos1 = blockpos1.offset(p_221271_.nextInt(3) - 1, (p_221271_.nextInt(3) - 1) * p_221271_.nextInt(3) / 2, p_221271_.nextInt(3) - 1);
                if (!p_221270_.getBlockState(blockpos1.below()).is(this) || p_221270_.getBlockState(blockpos1).isCollisionShapeFullBlock(p_221270_, blockpos1)) {
                    continue label49;
                }
            }

            BlockState blockstate1 = p_221270_.getBlockState(blockpos1);
            if (blockstate1.is(blockstate.getBlock()) && p_221271_.nextInt(10) == 0) {
                ((BonemealableBlock)blockstate.getBlock()).performBonemeal(p_221270_, p_221271_, blockpos1, blockstate1);
            }

            if (blockstate1.isAir()) {
                Holder<PlacedFeature> holder;
                if (p_221271_.nextInt(8) == 0) {
                    List<ConfiguredFeature<?, ?>> list = p_221270_.getBiome(blockpos1).value().getGenerationSettings().getFlowerFeatures();
                    if (list.isEmpty()) {
                        continue;
                    }

                    holder = ((RandomPatchConfiguration)list.get(0).config()).feature();
                } else {
                    if (!optional.isPresent()) {
                        continue;
                    }

                    holder = optional.get();
                }

                holder.value().place(p_221270_, p_221270_.getChunkSource().getGenerator(), p_221271_, blockpos1);
            }
        }
    }

    @Override
    public ItemStack getCloneItemStack(BlockState state, HitResult target, LevelReader level, BlockPos pos, Player player) {

        if (state.getBlock() instanceof ColoredGrass) {
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