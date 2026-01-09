package com.benbenlaw.core.network;

import com.benbenlaw.core.Core;
import com.benbenlaw.core.network.packets.SyncWhitelistMode;
import com.benbenlaw.core.network.packets.UpdateFilterFluidSlotsPacket;
import com.benbenlaw.core.network.packets.UpdateFilterSlotsPacket;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public class CoreNetworking {

    public static void registerNetworking(final RegisterPayloadHandlersEvent event) {

        final PayloadRegistrar registrar = event.registrar(Core.MOD_ID);

        registrar.playToServer(SyncWhitelistMode.TYPE, SyncWhitelistMode.STREAM_CODEC, SyncWhitelistMode.HANDLER);
        registrar.playToServer(UpdateFilterSlotsPacket.TYPE, UpdateFilterSlotsPacket.STREAM_CODEC, UpdateFilterSlotsPacket.HANDLER);
        registrar.playToServer(UpdateFilterFluidSlotsPacket.TYPE, UpdateFilterFluidSlotsPacket.STREAM_CODEC, UpdateFilterFluidSlotsPacket.HANDLER);


    }
}
