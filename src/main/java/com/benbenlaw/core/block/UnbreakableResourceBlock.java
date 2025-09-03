package com.benbenlaw.core.block;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Supplier;

public class UnbreakableResourceBlock extends Block {

    public int dropHeightModifier;
    public Supplier<Item> toolToCollectTheBlockAsItem;
    public TagKey<Item> toolToCollectTheBlockAsTag;
    public String particle;
    public int progress;
    private static boolean warnedAboutMissingParticle = false;
    public static final BooleanProperty RESTING = BooleanProperty.create("resting");

    public UnbreakableResourceBlock(Properties properties, int dropHeightModifier, String toolToCollectTheBlock, String particle) {
        super(properties);
        this.dropHeightModifier = dropHeightModifier;
        this.particle = particle;

        if (toolToCollectTheBlock.startsWith("#")) {
            this.toolToCollectTheBlockAsTag = TagKey.create(Registries.ITEM, ResourceLocation.parse(toolToCollectTheBlock.substring(1)));
        } else {
            this.toolToCollectTheBlockAsItem = () -> BuiltInRegistries.ITEM.get(ResourceLocation.parse(toolToCollectTheBlock));
        }
        this.registerDefaultState(this.stateDefinition.any().setValue(RESTING, Boolean.FALSE));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(RESTING);

    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        super.onRemove(state, level, pos, newState, movedByPiston);
    }

    @Override
    public void destroy(LevelAccessor level, BlockPos pos, BlockState state) {
        super.destroy(level, pos, state);
    }

    @Override
    protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        //level.setBlock(pos, state.setValue(OVERRIDE_DROPS, false), 3);
    }

    @Override
    protected boolean isRandomlyTicking(BlockState state) {
        return true;
    }

    @Override
    protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        level.setBlock(pos, state.setValue(RESTING, false), Block.UPDATE_ALL);
        super.tick(state, level, pos, random);
    }

    @Override
    public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        if (state.getValue(RESTING)) {
            return state;
        }
        return super.playerWillDestroy(level, pos, state, player);
    }



    @Override
    public boolean canEntityDestroy(BlockState state, BlockGetter level, BlockPos pos, Entity entity) {
        return super.canEntityDestroy(state, level, pos, entity);
    }

    @Override public float defaultDestroyTime() {
        if (this.defaultBlockState().getValue(RESTING)) {
            return 0.0f;
        }
        return super.defaultDestroyTime();
    }

    @Override
    public boolean onDestroyedByPlayer(BlockState state, Level level, BlockPos pos, Player player, boolean willHarvest, FluidState fluid) {
        if (state.getValue(RESTING)) {
            return false;
        }
        return super.onDestroyedByPlayer(state, level, pos, player, willHarvest, fluid);
    }

    @Override
    public void playerDestroy(@NotNull Level level, @NotNull Player player, BlockPos pos, @NotNull BlockState state, @Nullable BlockEntity blockEntity, @NotNull ItemStack tool) {

        if (state.getValue(RESTING)) {
            return;
        }

        boolean isCorrectTool = false;

        if (toolToCollectTheBlockAsTag != null) {
            isCorrectTool = tool.is(toolToCollectTheBlockAsTag);
        }

        if (toolToCollectTheBlockAsItem != null) {
            isCorrectTool = isCorrectTool || ItemStack.isSameItemSameComponents(tool, new ItemStack(toolToCollectTheBlockAsItem.get()));
        }

        if (!isCorrectTool) {
            level.setBlock(pos, this.defaultBlockState().setValue(RESTING, true), Block.UPDATE_ALL);
            level.scheduleTick(pos, this, 20);
        }
        super.playerDestroy(level, player, pos, state, blockEntity, tool);
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        if (toolToCollectTheBlockAsItem != null) {
            Component name = toolToCollectTheBlockAsItem.get().getName(toolToCollectTheBlockAsItem.get().getDefaultInstance());
            tooltipComponents.add(Component.translatable("tooltips.bblcore.block.unbreakable_resource_block_tool", name).withStyle(ChatFormatting.GRAY));
        }

        if (toolToCollectTheBlockAsTag != null) {
            String tag = toolToCollectTheBlockAsTag.toString();
            tooltipComponents.add(Component.translatable("tooltips.bblcore.block.unbreakable_resource_block_tool_tag", tag).withStyle(ChatFormatting.GRAY));
        }
    }

    @Override
    protected float getDestroyProgress(BlockState state, Player player, BlockGetter level, BlockPos pos) {
        if (state.getValue(RESTING)) {
            return 0.0f;
        }
        return super.getDestroyProgress(state, player, level, pos);
    }

    @Override
    public boolean canHarvestBlock(BlockState state, BlockGetter level, BlockPos pos, Player player) {
        if (state.getValue(RESTING)) {
            return false;
        }
        return super.canHarvestBlock(state, level, pos, player);
    }

    @Override
    public float getSpeedFactor() {
        if (this.defaultBlockState().getValue(RESTING)) {
            return 0.0f;
        }
        return super.getSpeedFactor();
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (random.nextFloat() < 0.5F) {

            ParticleOptions particleType = (ParticleOptions) BuiltInRegistries.PARTICLE_TYPE.get(ResourceLocation.parse(particle));
            if (particleType == null) {
                if (!warnedAboutMissingParticle) {
                    System.out.println("Particle not found, defaulting to minecraft:flame!");
                    warnedAboutMissingParticle = true;
                }
                particleType = ParticleTypes.FLAME;
            }

            level.addParticle(
                    particleType,
                    pos.getX() + 0.5,
                    pos.getY() + 0.5,
                    pos.getZ() + 0.5,
                    0.0D,
                    0.0D,
                    0.0D
            );
        }
    }
}
