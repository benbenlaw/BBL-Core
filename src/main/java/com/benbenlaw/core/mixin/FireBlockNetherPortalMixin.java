package com.benbenlaw.core.mixin;

import com.benbenlaw.core.util.CoreTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(value = BaseFireBlock.class)
public class FireBlockNetherPortalMixin {
    @ModifyVariable(method = "isPortal", at = @At(value = "INVOKE", target="Lnet/minecraft/core/Direction;values()[Lnet/minecraft/core/Direction;"))
    private static boolean BaseFireBlock_isPortal(boolean bl, Level level, BlockPos blockPos, Direction direction) {
        BlockPos.MutableBlockPos mutableBlockPos = blockPos.mutable();
        Direction[] var5 = Direction.values();
        for (Direction direction2 : var5) {
            if (level.getBlockState(mutableBlockPos.set(blockPos).move(direction2)).is(CoreTags.Blocks.NETHER_PORTAL_FRAME)) {
                return true;
            }
        }

        return false;
    }
}