package com.benbenlaw.core.integration.kubejs;

/*
import com.benbenlaw.core.block.UnbreakableResourceBlock;
import dev.latvian.mods.kubejs.block.BlockBuilder;
import dev.latvian.mods.kubejs.typings.Info;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

public class ResourceBlockBuilder extends BlockBuilder {

    private int dropHeightModifier = 0;
    private String toolToCollectTheBlock = "";
    private String lootTable = "";
    private String particle = "";
    public ResourceBlockBuilder(ResourceLocation i) {
        super(i);
    }

    @Info("The height modifier for the block to drop. Default is 0. Can be negative")
    public ResourceBlockBuilder dropHeightModifier(int dropHeightModifier) {
        this.dropHeightModifier = dropHeightModifier;
        return this;
    }

    @Info("The tool to collect the block. Can be an item or a tag, use # to specify a tag, If broken without the tool drops the defined loot table")
    public ResourceBlockBuilder toolToCollectTheBlock(String toolToCollectTheBlock) {
        this.toolToCollectTheBlock = toolToCollectTheBlock;
        return this;
    }

    @Info("The loot table to use when the block is broken without the tool eg, minecraft:blocks/stone ")
    public ResourceBlockBuilder resourceBlockLootTable(String lootTable) {
        this.lootTable = lootTable;
        return this;
    }

    @Info("The particle the block gives off, optional defaults to minecraft:flames")
    public ResourceBlockBuilder particle(String particle) {
        this.particle = particle;
        return this;
    }

    @Override
    public Block createObject() {
        return new UnbreakableResourceBlock(createProperties(),
                dropHeightModifier, toolToCollectTheBlock, lootTable, particle);
    }
}

 */
