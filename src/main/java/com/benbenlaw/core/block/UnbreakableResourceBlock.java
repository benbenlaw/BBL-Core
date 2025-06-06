package com.benbenlaw.core.block;

import com.benbenlaw.core.util.FakePlayerUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
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
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.neoforged.neoforge.common.util.FakePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class UnbreakableResourceBlock extends Block {

    public int dropHeightModifier;
    public Supplier<Item> toolToCollectTheBlockAsItem;
    public TagKey<Item> toolToCollectTheBlockAsTag;
    public String lootTable;
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
            this.toolToCollectTheBlockAsItem = () -> BuiltInRegistries.ITEM.getValue(ResourceLocation.parse(toolToCollectTheBlock));
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

        if (!isCorrectTool) {
            dropResources(state, level, (dropPos), blockEntity, player, tool);
            level.setBlockAndUpdate(pos, this.defaultBlockState());
        } else {
            popResource(level, pos, new ItemStack(this.asItem(), 1));
        }
    }

    @Override
    protected @NotNull List<ItemStack> getDrops(BlockState state, LootParams.Builder params) {
        ServerLevel level = params.getLevel();

        if (fakePlayer == null) {
            fakePlayer = FakePlayerUtil.createFakePlayer(level, "resource_block_fake_player");
        }

        LootParams lootparams =  (new LootParams.Builder((ServerLevel) fakePlayer.level())).withParameter(LootContextParams.THIS_ENTITY, fakePlayer).withParameter(LootContextParams.ORIGIN, fakePlayer.position()).create(LootContextParamSets.GIFT);
        LootTable table = level.getServer().reloadableRegistries().getLootTable(ResourceKey.create(Registries.LOOT_TABLE,
                ResourceLocation.parse(lootTable)));

        List <ItemStack> drops = new java.util.ArrayList<>(List.of(ItemStack.EMPTY));
        List <ItemStack> loot = table.getRandomItems(lootparams);
        drops.addAll(loot);

        return drops;
    }

    /* block now need there own item to all tooltips WHHYHYYY

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext tooltipContext, TooltipDisplay tooltipDisplay, Consumer<Component> tooltipAdder, TooltipFlag flag) {

        if (toolToCollectTheBlockAsItem != null) {
            Component name = toolToCollectTheBlockAsItem.get().getName(toolToCollectTheBlockAsItem.get().getDefaultInstance());
            tooltipAdder.accept(Component.translatable("tooltips.bblcore.block.unbreakable_resource_block_tool", name).withStyle(ChatFormatting.GRAY));
        }

        if (toolToCollectTheBlockAsTag != null) {
            String tag = toolToCollectTheBlockAsTag.toString();
            tooltipAdder.accept(Component.translatable("tooltips.bblcore.block.unbreakable_resource_block_tool_tag", tag).withStyle(ChatFormatting.GRAY));
        }
    }

     */

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (random.nextFloat() < 0.5F) {

            ParticleOptions particleType = (ParticleOptions) BuiltInRegistries.PARTICLE_TYPE.getValue(ResourceLocation.parse(particle));
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
}
