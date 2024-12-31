package com.benbenlaw.core.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;

public record ChanceResult(ItemStack stack, float chance) {
    public static final ChanceResult EMPTY = new ChanceResult(ItemStack.EMPTY, 1);
    public static final Codec<ChanceResult> CODEC = RecordCodecBuilder.create(inst -> inst.group(
            ItemStack.CODEC.fieldOf("item").forGetter(ChanceResult::stack),
            Codec.FLOAT.optionalFieldOf("chance", 1.0f).forGetter(ChanceResult::chance)
    ).apply(inst, ChanceResult::new));


    public ItemStack rollOutput(RandomSource rand) {
        int outputAmount = stack.getCount();
        for (int roll = 0; roll < stack.getCount(); roll++)
            if (rand.nextFloat() > chance)
                outputAmount--;
        if (outputAmount == 0)
            return ItemStack.EMPTY;
        ItemStack out = stack.copy();
        out.setCount(outputAmount);
        return out;
    }

    public void write(RegistryFriendlyByteBuf buffer) {
        ItemStack.STREAM_CODEC.encode(buffer, stack());
        buffer.writeFloat(chance());
    }

    public static ChanceResult read(RegistryFriendlyByteBuf buffer) {
        return new ChanceResult(ItemStack.STREAM_CODEC.decode(buffer), buffer.readFloat());
    }
}