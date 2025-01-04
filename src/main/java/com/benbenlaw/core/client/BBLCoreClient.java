package com.benbenlaw.core.client;

import com.benbenlaw.core.Core;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.Mod;

@Mod(value = Core.MOD_ID, dist = Dist.CLIENT)
public class BBLCoreClient {
    public static boolean isCreatingNewWorld;
}
