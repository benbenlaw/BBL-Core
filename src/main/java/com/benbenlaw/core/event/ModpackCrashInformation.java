package com.benbenlaw.core.event;

import com.benbenlaw.core.config.CoreModpackConfig;
import net.neoforged.fml.CrashReportCallables;

public class ModpackCrashInformation {

    public static void register() {

        if (CoreModpackConfig.modpackName.get().isEmpty() || CoreModpackConfig.modpackVersion.get().isEmpty()) {
            return;
        }

        CrashReportCallables.registerCrashCallable("Modpack Information",
                () -> "This Crash is from " + CoreModpackConfig.modpackName.get() + " " + CoreModpackConfig.modpackVersion.get());
    }
}