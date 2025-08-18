package com.benbenlaw.core.block;

import com.benbenlaw.core.util.BlockInformation;
import com.benbenlaw.core.util.FakePlayerUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.CommonHooks;
import net.neoforged.neoforge.common.util.FakePlayer;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

import static com.benbenlaw.core.event.UnbreakableBlockReplaceEvent.blockInformationMap;

public class UnbreakableResourceBlock extends Block {

    public int dropHeightModifier;
    public Supplier<Item> toolToCollectTheBlockAsItem;
    public TagKey<Item> toolToCollectTheBlockAsTag;
    public String particle;
    private static boolean warnedAboutMissingParticle = false;
    public UnbreakableResourceBlock(Properties properties, int dropHeightModifier, String toolToCollectTheBlock, String particle) {
        super(properties);
        this.dropHeightModifier = dropHeightModifier;
        this.particle = particle;

        if (toolToCollectTheBlock.startsWith("#")) {
            this.toolToCollectTheBlockAsTag = TagKey.create(Registries.ITEM, ResourceLocation.parse(toolToCollectTheBlock.substring(1)));
        } else {
            this.toolToCollectTheBlockAsItem = () -> BuiltInRegistries.ITEM.get(ResourceLocation.parse(toolToCollectTheBlock));
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
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
    public void playerDestroy(@NotNull Level level, @NotNull Player player, BlockPos pos, @NotNull BlockState state, @Nullable BlockEntity blockEntity, @NotNull ItemStack tool) {

        boolean isCorrectTool = false;

        if (toolToCollectTheBlockAsTag != null) {
            isCorrectTool = tool.is(toolToCollectTheBlockAsTag);
        }

        if (toolToCollectTheBlockAsItem != null) {
            isCorrectTool = isCorrectTool || ItemStack.isSameItemSameComponents(tool, new ItemStack(toolToCollectTheBlockAsItem.get()));
        }

        if (!isCorrectTool) {
            long delay = 10 + Objects.requireNonNull(level.getServer()).getTickCount();
            blockInformationMap.put(pos, new BlockInformation(state, level, delay));
            level.setBlock(pos, Blocks.BARRIER.defaultBlockState(), Block.UPDATE_ALL);
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
