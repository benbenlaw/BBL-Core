package com.benbenlaw.core;

import com.benbenlaw.core.config.ColorTintIndexConfig;
import com.benbenlaw.core.config.CoreStartupConfig;
import com.benbenlaw.core.item.CoreDataComponents;
import com.benbenlaw.core.item.CoreItems;
import com.benbenlaw.core.loot.condition.CoreLootModifierCondition;
import com.benbenlaw.core.loot.modifier.CoreLootModifiers;
import com.benbenlaw.core.recipe.CoreConditions;
import com.benbenlaw.core.util.ColorHandler;
import com.mojang.logging.LogUtils;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.loading.FMLEnvironment;
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
        CoreLootModifierCondition.LOOT_CONDITION_TYPES.register(eventBus);
        CoreLootModifiers.LOOT_MODIFIER_SERIALIZERS.register(eventBus);


        modContainer.registerConfig(ModConfig.Type.STARTUP, ColorTintIndexConfig.SPEC, "bbl/core/color_index.toml");
        modContainer.registerConfig(ModConfig.Type.STARTUP, CoreStartupConfig.SPEC, "bbl/core/startup.toml");

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
