package com.benbenlaw.core;

import com.benbenlaw.core.config.DimensionConfig;
import com.benbenlaw.core.config.ModpackConfig;
import com.benbenlaw.core.config.StartupConfig;
import com.benbenlaw.core.event.ModpackCrashInformation;
import com.benbenlaw.core.item.CoreItems;
import com.benbenlaw.core.loot.condition.CoreLootModifierCondition;
import com.benbenlaw.core.loot.modifier.CoreLootModifiers;
import com.benbenlaw.core.network.CoreNetworking;
import com.benbenlaw.core.recipe.CoreRecipeConditions;
import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(Core.MOD_ID)
public class Core {
    public static final String MOD_ID = "bblcore";
    public static final Logger LOGGER = LogUtils.getLogger();


    public Core(final IEventBus eventBus, final ModContainer modContainer) {
        //TestItem.ITEMS.register(eventBus);
        //TestBlock.BLOCKS.register(eventBus);
        //TestBlockEntities.BLOCK_ENTITIES.register(eventBus);
        //TestFluid.FLUIDS.register(modEventBus);


        //** DO NOT DISABLE THIS LINE **//

        //Global Resource Tags

        CoreItems.ITEMS.register(eventBus);

        CoreRecipeConditions.CONDITIONALS.register(eventBus);
        CoreLootModifierCondition.LOOT_CONDITION_TYPES.register(eventBus);
        CoreLootModifiers.LOOT_MODIFIER_SERIALIZERS.register(eventBus);

        //Configs
        modContainer.registerConfig(ModConfig.Type.STARTUP, StartupConfig.SPEC, "bbl/core/startup.toml");
        modContainer.registerConfig(ModConfig.Type.STARTUP, ModpackConfig.SPEC, "bbl/core/modpack.toml");
        modContainer.registerConfig(ModConfig.Type.STARTUP, DimensionConfig.SPEC, "bbl/core/dimensions.toml");

        //Modpack Crash Information
        ModpackCrashInformation.register();

        eventBus.addListener(this::addItemToCreativeTab);
        eventBus.addListener(this::registerNetworking);

    }

    private void addItemToCreativeTab(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.INGREDIENTS) {
            event.accept(CoreItems.UPGRADE_BASE.get());
        }
    }

    public static ResourceLocation rl(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }

    public void registerNetworking(RegisterPayloadHandlersEvent event) {
        CoreNetworking.registerNetworking(event);
    }
}
