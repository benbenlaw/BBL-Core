package com.benbenlaw.core.network.packets;

import com.benbenlaw.core.Core;
import com.benbenlaw.core.screen.util.slot.FilterFluidSlot;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.network.handling.IPayloadHandler;

public record UpdateFilterFluidSlotsPacket(int slot, FluidStack stack) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<UpdateFilterFluidSlotsPacket> TYPE = new CustomPacketPayload.Type(Core.identifier("update_filter_fluid_slots"));
    public static final IPayloadHandler<UpdateFilterFluidSlotsPacket> HANDLER = (packet, context) -> {
        Player player = context.player();
        AbstractContainerMenu menu = player.containerMenu;
        Slot slot = menu.getSlot(packet.slot);
        if (slot instanceof FilterFluidSlot filterFluidSlot) {
            filterFluidSlot.set(packet.stack);
            context.player().playSound(SoundEvents.LEVER_CLICK, (float) SoundSource.PLAYERS.ordinal(), 1.0F);
        }

    };
    public static final StreamCodec<RegistryFriendlyByteBuf, UpdateFilterFluidSlotsPacket> STREAM_CODEC;

    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    static {
        STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.INT, UpdateFilterFluidSlotsPacket::slot, FluidStack.STREAM_CODEC, UpdateFilterFluidSlotsPacket::stack, UpdateFilterFluidSlotsPacket::new);
    }
}