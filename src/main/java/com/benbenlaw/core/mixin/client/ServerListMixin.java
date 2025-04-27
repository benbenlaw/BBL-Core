package com.benbenlaw.core.mixin.client;

import com.benbenlaw.core.config.CoreModpackConfig;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.ServerList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(ServerList.class)
public class ServerListMixin {

    @Inject(method = "load", at = @At("RETURN"))
    private void injectCustomServer(CallbackInfo ci) {

        if (!CoreModpackConfig.serverName.get().isEmpty() && !CoreModpackConfig.serverIP.get().isEmpty()) {

            ServerList serverList = (ServerList) (Object) this;
            List<ServerData> servers = ((ServerListAccessor) serverList).getServerList();

            String serverName = CoreModpackConfig.serverName.get();
            String serverIP = CoreModpackConfig.serverIP.get();

            servers.removeIf(server -> server.ip.equals(serverIP));
            ServerData customServer = new ServerData(serverName, serverIP, ServerData.Type.OTHER);
            servers.addFirst(customServer);
            serverList.save();
        }
    }
}

