package com.benbenlaw.core.mixin;

import com.benbenlaw.core.util.CoreTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.portal.PortalShape;
import net.neoforged.neoforge.common.extensions.IBlockExtension;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = PortalShape.class)
public class PortalTagForNetherPortalsMixin {
    @Shadow @Final @Mutable
    private static BlockBehaviour.StatePredicate FRAME;

    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void modifyFrame(CallbackInfo cir) {
        FRAME = (BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) ->
                blockState.is(CoreTags.Blocks.NETHER_PORTAL_FRAME);
    }
}