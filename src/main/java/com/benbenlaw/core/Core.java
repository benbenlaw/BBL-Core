package com.benbenlaw.core;

import com.benbenlaw.core.block.TestBlock;
import com.benbenlaw.core.config.ColorTintIndexConfig;
import com.benbenlaw.core.fluid.TestFluid;
import com.benbenlaw.core.item.CoreCreativeTab;
import com.benbenlaw.core.item.CoreDataComponents;
import com.benbenlaw.core.item.CoreItems;
import com.benbenlaw.core.item.TestItem;
import com.benbenlaw.core.recipe.CoreConditions;
import com.benbenlaw.core.recipe.WorldTypeCondition;
import com.benbenlaw.core.util.ColorHandler;
import com.mojang.logging.LogUtils;
import net.minecraft.client.telemetry.events.WorldLoadEvent;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(Core.MOD_ID)
public class Core {
    public static final String MOD_ID = "bblcore";
    public static final Logger LOGGER = LogUtils.getLogger();


    public Core(final IEventBus eventBus, final ModContainer modContainer) {
        //TestItem.ITEMS.register(eventBus);
        //TestBlock.BLOCKS.register(eventBus);
        //TestFluid.FLUIDS.register(modEventBus);


        //** DO NOT DISABLE THIS LINE **//

        CoreDataComponents.COMPONENTS.register(eventBus);
        CoreItems.ITEMS.register(eventBus);
        CoreConditions.CONDITIONALS.register(eventBus);


        modContainer.registerConfig(ModConfig.Type.STARTUP, ColorTintIndexConfig.SPEC, "bbl/core/color_index.toml");

        eventBus.addListener(this::addItemToCreativeTab);

        if (FMLEnvironment.dist == Dist.CLIENT) {
            eventBus.register(new ColorHandler());
        }
    }

    private void addItemToCreativeTab(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.INGREDIENTS) {
            event.accept(CoreItems.UPGRADE_BASE.get());
        }
    }


        /*

        @SubscribeEvent
        public static void onClientExtensions(RegisterClientExtensionsEvent event) {
            event.registerFluidType(TestFluid.LIME_WATER.getFluidType().getClientExtensions(),
                    TestFluid.LIME_WATER.getFluidType());

            event.registerFluidType(TestFluid.PINK_WATER.getFluidType().getClientExtensions(),
                    TestFluid.PINK_WATER.getFluidType());

        }

         */
}
