package com.benbenlaw.core.event;

import com.benbenlaw.core.config.ModpackConfig;
import net.neoforged.fml.CrashReportCallables;

public class ModpackCrashInformation {

    public static void register() {

        if (ModpackConfig.modpackName.get().isEmpty() || ModpackConfig.modpackVersion.get().isEmpty()) {
            return;
        }

        CrashReportCallables.registerCrashCallable("Modpack Information",
                () -> "This Crash is from " + ModpackConfig.modpackName.get() + " " + ModpackConfig.modpackVersion.get());
    }
}