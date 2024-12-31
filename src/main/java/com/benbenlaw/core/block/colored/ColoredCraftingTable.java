package com.benbenlaw.core.block.colored;

import com.benbenlaw.core.block.colored.util.ColorMap;
import com.benbenlaw.core.block.colored.util.IColored;
import com.benbenlaw.core.item.CoreDataComponents;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.CraftingMenu;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ColoredCraftingTable extends ColoredBlock implements IColored {

    public ColoredCraftingTable(Properties properties) {
        super(properties);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState p_52233_, Level p_52234_, BlockPos p_52235_, Player p_52236_, BlockHitResult p_52238_) {
        if (p_52234_.isClientSide) {
            return InteractionResult.SUCCESS;
        } else {
            p_52236_.openMenu(p_52233_.getMenuProvider(p_52234_, p_52235_));
            p_52236_.awardStat(Stats.INTERACT_WITH_CRAFTING_TABLE);
            return InteractionResult.CONSUME;
        }
    }

    @Override
    protected MenuProvider getMenuProvider(BlockState p_52240_, Level p_52241_, BlockPos p_52242_) {
        return new SimpleMenuProvider(
                (p_52229_, p_52230_, p_52231_) -> new CraftingMenu(p_52229_, p_52230_, ContainerLevelAccess.create(p_52241_, p_52242_)),
                Component.translatable("core.block.colored_crafting_table")
        );
    }
}