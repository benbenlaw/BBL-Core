package com.benbenlaw.core.mixin;

import com.benbenlaw.core.block.brightable.BrightCraftingTable;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.CraftingMenu;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = CraftingMenu.class)
public class ColoredCraftingTableValidForPlayer {
    @Final
    @Shadow
    private ContainerLevelAccess access;

    @Inject(method = "stillValid", at = @At("HEAD"), cancellable = true)
    private void stillValid(Player player, CallbackInfoReturnable<Boolean> cir) {

        if (access.evaluate((world, pos) -> world.getBlockState(pos).getBlock() instanceof BrightCraftingTable, true)) {
            cir.setReturnValue(true);
        }
    }
}