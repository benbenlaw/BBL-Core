package com.benbenlaw.core.mixin.access;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.chunk.ChunkGenerator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ChunkGenerator.class)
public interface ChunkGeneratorAccess {
    @Invoker("codec")
    MapCodec<? extends ChunkGenerator> bblcore$getCodec();
}
