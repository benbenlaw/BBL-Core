package com.benbenlaw.core.item;

import com.benbenlaw.core.Core;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;


public class CoreCreativeTab {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Core.MOD_ID);

    /*
    public static final Supplier<CreativeModeTab> OPOLIS_UTILITIES_TAB = CREATIVE_MODE_TABS.register("opolisutilities", () -> CreativeModeTab.builder()
            .icon(() -> new ItemStack(TestBlock.COLORED_BLOCK.get()))



            .title(Component.translatable("itemGroup.opolisutilities"))
            .displayItems((parameters, output) -> {

                ColorMap.COLOR_MAP.forEach((color, value) -> {
                    ItemStack colorBlock = new ItemStack(TestBlock.COLORED_BLOCK.get());
                    ItemStack planks = new ItemStack(TestBlock.PLANKS.get());

                    colorBlock.set(CoreDataComponents.COLOR, color.toString());
                    planks.set(CoreDataComponents.COLOR, color.toString());

                    colorBlock.set(CoreDataComponents.LIT, false);
                    planks.set(CoreDataComponents.LIT, false);

                    output.accept(colorBlock);
                    output.accept(planks);
                        }
                );



            }).build());





     */

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
    



}
