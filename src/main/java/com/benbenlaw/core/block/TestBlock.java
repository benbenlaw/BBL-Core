package com.benbenlaw.core.block;

import com.benbenlaw.core.Core;
import com.benbenlaw.core.block.brightable.BrightBlock;
import com.benbenlaw.core.block.colored.ColoredBlock;
import com.benbenlaw.core.block.colored.ColoredStairs;
import com.benbenlaw.core.block.colored.ColoredWall;
import com.benbenlaw.core.block.colored.flammable.FlammableColoredLog;
import com.benbenlaw.core.item.TestItem;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;
import java.util.function.ToIntFunction;

public class TestBlock {

    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.createBlocks(Core.MOD_ID);

    public static final DeferredBlock<Block> COLORED_BLOCK = registerBlock("colored_block",
            () -> new BrightBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.DIRT).sound(SoundType.STONE)
                    .instabreak()
                    .noOcclusion())
    );
    public static final DeferredBlock<Block> RESOURCE_BLOCK = registerBlock("resource_block",
            () -> new UnbreakableResourceBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.DIRT).sound(SoundType.STONE),
                    1, "minecraft:dirt", "minecraft:blocks/diamond_ore")
    );

    public static final DeferredBlock<Block> RESOURCE_BLOCK_2 = registerBlock("resource_block_2",
            () -> new UnbreakableResourceBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.DIRT).sound(SoundType.STONE),
                    1,"#minecraft:planks", "minecraft:blocks/emerald_ore")
    );

    /*

    public static final DeferredBlock<Block> TANK = registerBlockWithoutBlockItem("tank",
            () -> new TankBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.DIRT).sound(SoundType.STONE)
                    .instabreak()
    ));


    public static final DeferredBlock<Block> COLORED_BLOCK = registerBlockWithoutBlockItem("colored_block",
            () -> new ColoredBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.DIRT).sound(SoundType.STONE)
                    .lightLevel(litBlockEmission())
                    .instabreak()
                    .noOcclusion())
    );

    public static final DeferredBlock<Block> COLORED_STONE_WALL = registerBlockWithoutBlockItem("colored_stone_wall",
            () -> new ColoredWall(BlockBehaviour.Properties.ofFullCopy(Blocks.COBBLESTONE_WALL).sound(SoundType.STONE)
                    //.lightLevel(litBlockEmission())
                    .noOcclusion()));


    public static final DeferredBlock<Block> COLORED_BLOCK_STAIRS = registerBlockWithoutBlockItem("colored_block_stairs",
            () -> new ColoredStairs(TestBlock.COLORED_BLOCK.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(Blocks.DIRT).sound(SoundType.STONE)
                    .lightLevel(litBlockEmission())
                    .instabreak()
                    .noOcclusion())
    );

    public static final DeferredBlock<Block> LOG = registerBlockWithoutBlockItem("log",
            () -> new FlammableColoredLog(BlockBehaviour.Properties.ofFullCopy(Blocks.DIRT).sound(SoundType.STONE)
                    .lightLevel(litBlockEmission())
                    .instabreak()
                    .noOcclusion())
    );

    public static final DeferredBlock<Block> PLANKS = registerBlockWithoutBlockItem("planks",
            () -> new ColoredBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.DIRT).sound(SoundType.STONE)
                    .lightLevel(litBlockEmission())
                    .instabreak()

                    .noOcclusion()));

    public static final DeferredBlock<Block> BRICKS = registerBlockWithoutBlockItem("bricks",
            () -> new ColoredBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.DIRT).sound(SoundType.STONE)
                    .lightLevel(litBlockEmission())
                    .instabreak()
                    .noOcclusion()));

     */



    private static <T extends Block> DeferredBlock<T> registerBlockWithoutBlockItem(String name, Supplier<T> block) {
        return (DeferredBlock<T>) BLOCKS.register(name, block);
    }

    private static <T extends Block> DeferredBlock<T> registerBlock(String name, Supplier<T> block) {
        DeferredBlock<T> toReturn = (DeferredBlock<T>) BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> void registerBlockItem(String name, DeferredBlock<T> block) {
        TestItem.ITEMS.register(name, () -> new BlockItem(block.get(),
                new Item.Properties()));

    }

    private static ToIntFunction<BlockState> litBlockEmission() {
        return (blockState) -> blockState.getValue(BlockStateProperties.LIT) ? 15 : 0;
    }


    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }



}


