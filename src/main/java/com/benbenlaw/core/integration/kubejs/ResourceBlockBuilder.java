package com.benbenlaw.core.integration.kubejs;

import com.benbenlaw.core.block.UnbreakableResourceBlock;
import com.sun.jna.platform.unix.Resource;
import dev.latvian.mods.kubejs.block.BlockBuilder;
import dev.latvian.mods.kubejs.block.KubeJSBlockProperties;
import dev.latvian.mods.kubejs.typings.Info;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.jetbrains.annotations.Blocking;
import org.openjdk.nashorn.internal.objects.annotations.Setter;

public class ResourceBlockBuilder extends BlockBuilder {

    private int dropHeightModifier = 0;
    private String toolToCollectTheBlock = "";
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

    @Info("The particle the block gives off, optional defaults to minecraft:flames")
    public ResourceBlockBuilder particle(String particle) {
        this.particle = particle;
        return this;
    }

    @Override
    public Block createObject() {
        return new UnbreakableResourceBlock(createProperties(),
                dropHeightModifier, toolToCollectTheBlock, particle);
    }
}
