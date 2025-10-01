package com.benbenlaw.core.network.packets;

import com.benbenlaw.core.Core;
import com.benbenlaw.core.screen.util.slot.FilterSlot;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadHandler;

public record UpdateFilterSlotsPacket(int slot, ItemStack stack) implements CustomPacketPayload {

    public static final Type<UpdateFilterSlotsPacket> TYPE = new Type<>(Core.rl("update_filter_slots"));

    public static final IPayloadHandler<UpdateFilterSlotsPacket> HANDLER = (packet, context) -> {
        Player player = context.player();
        AbstractContainerMenu menu = player.containerMenu;

        Slot slot = menu.getSlot(packet.slot);
        if (slot instanceof FilterSlot) {
            slot.set(packet.stack);
            context.player().playSound(SoundEvents.LEVER_CLICK, SoundSource.PLAYERS.ordinal(), 1.0f);
        }
    };

    public static final StreamCodec<RegistryFriendlyByteBuf, UpdateFilterSlotsPacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, UpdateFilterSlotsPacket::slot,
            ItemStack.STREAM_CODEC, UpdateFilterSlotsPacket::stack,
            UpdateFilterSlotsPacket::new
    );


    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
