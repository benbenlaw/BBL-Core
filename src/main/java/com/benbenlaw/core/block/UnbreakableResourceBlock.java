package com.benbenlaw.core.block;

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

public class UnbreakableResourceBlock extends Block {

    public int dropHeightModifier;
    public Supplier<Item> toolToCollectTheBlockAsItem;
    public TagKey<Item> toolToCollectTheBlockAsTag;
    public static String lootTable;
    public String particle;
    public FakePlayer fakePlayer;
    private static boolean warnedAboutMissingParticle = false;
    public UnbreakableResourceBlock(Properties properties, int dropHeightModifier, String toolToCollectTheBlock, String lootTable, String particle) {
        super(properties);
        this.dropHeightModifier = dropHeightModifier;
        this.lootTable = lootTable;
        this.particle = particle;

        if (toolToCollectTheBlock.startsWith("#")) {
            this.toolToCollectTheBlockAsTag = TagKey.create(Registries.ITEM, ResourceLocation.parse(toolToCollectTheBlock.substring(1)));
        } else {
            this.toolToCollectTheBlockAsItem = () -> BuiltInRegistries.ITEM.get(ResourceLocation.parse(toolToCollectTheBlock));
        }
    }


    @Override
    public void destroy(LevelAccessor level, BlockPos pos, BlockState state) {
        super.destroy(level, pos, state);
    }

    @Override
    public void playerDestroy(@NotNull Level level, @NotNull Player player, BlockPos pos, @NotNull BlockState state, @Nullable BlockEntity blockEntity, @NotNull ItemStack tool) {


        BlockPos dropPos = new BlockPos(pos.getX(), pos.getY() + dropHeightModifier, pos.getZ());
        boolean isCorrectTool = false;

        if (toolToCollectTheBlockAsTag != null) {
            isCorrectTool = tool.is(this.toolToCollectTheBlockAsTag);
        }

        if (toolToCollectTheBlockAsItem != null) {
            isCorrectTool = tool.getItem() == toolToCollectTheBlockAsItem.get();
        }

        List<ItemStack> drops = getLootDrops(state, blockEntity, pos, player, tool, level);

        if (!isCorrectTool) {

            for (ItemStack drop : drops) {
                if (drop.isEmpty()) continue;
                popOutTheItem(level, dropPos, drop);
            }
            level.setBlockAndUpdate(pos, this.defaultBlockState());
        } else {
            popResource(level, pos, new ItemStack(this.asItem(), 1));
        }
    }


    public static List<ItemStack> getLootDrops(BlockState state, BlockEntity entity, BlockPos pos, Player player, ItemStack tool, Level level) {
        LootParams.Builder lootParams = new LootParams.Builder((ServerLevel) level)
                .withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(pos))
                .withParameter(LootContextParams.TOOL, tool)
                .withParameter(LootContextParams.BLOCK_ENTITY, entity)
                .withParameter(LootContextParams.THIS_ENTITY, player)
                .withParameter(LootContextParams.BLOCK_STATE, state);

        LootTable lootTableName = level.getServer().reloadableRegistries()
                .getLootTable(ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.parse(lootTable)));

        List<ItemStack> finalDrops = new java.util.ArrayList<>(List.of(ItemStack.EMPTY));
        List<ItemStack> loot = lootTableName.getRandomItems(lootParams.create(LootContextParamSet.builder().build()));
        finalDrops.addAll(loot);

        return finalDrops;

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
                    pos.getY() + 1.5,
                    pos.getZ() + 0.5,
                    0.0D,
                    0.0D,
                    0.0D
            );
        }
    }


    public static void popOutTheItem(Level level, BlockPos blockPos, ItemStack itemStack) {

        Vec3 vec3 = Vec3.atLowerCornerWithOffset(blockPos, 0.5, 0.5, 0.5).offsetRandom(level.random, 0.7F);
        ItemStack itemstack1 = itemStack.copy();
        ItemEntity itementity = new ItemEntity(level, vec3.x(), vec3.y(), vec3.z(), itemstack1);
        itementity.setDefaultPickUpDelay();
        level.addFreshEntity(itementity);
    }
}
