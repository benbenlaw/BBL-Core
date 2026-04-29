package com.benbenlaw.core.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;

public record ChanceResult(ItemStackTemplate template, float chance) {

    public static final Codec<ChanceResult> CODEC = RecordCodecBuilder.create(inst -> inst.group(
            ItemStackTemplate.CODEC.fieldOf("item").forGetter(ChanceResult::template),
            Codec.FLOAT.optionalFieldOf("chance", 1.0f).forGetter(ChanceResult::chance)
    ).apply(inst, ChanceResult::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, ChanceResult> STREAM_CODEC = StreamCodec.of(
            (buf, value) ->
                    value.write(buf), ChanceResult::read);

    public ItemStack rollOutput(RandomSource rand) {
        ItemStack stack = template.create();
        int outputAmount = stack.getCount();
        for (int roll = 0; roll < stack.getCount(); roll++) {
            if (rand.nextFloat() > chance) outputAmount--;
        }
        if (outputAmount == 0) return ItemStack.EMPTY;
        ItemStack out = stack.copy();
        out.setCount(outputAmount);
        return out;
    }

    public void write(RegistryFriendlyByteBuf buffer) {
        ItemStackTemplate.STREAM_CODEC.encode(buffer, template);
        buffer.writeFloat(chance);
    }

    public static ChanceResult read(RegistryFriendlyByteBuf buffer) {
        return new ChanceResult(ItemStackTemplate.STREAM_CODEC.decode(buffer), buffer.readFloat());
    }
}