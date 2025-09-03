package com.benbenlaw.core.mixin.compat;

import com.benbenlaw.core.block.UnbreakableResourceBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.util.FakePlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static com.benbenlaw.core.block.UnbreakableResourceBlock.RESTING;

@Mixin(targets = "com.direwolf20.justdirethings.common.blockentities.BlockBreakerT1BE")
public class JustDireBlockBreakersMixin {

    @Unique
    private static Class<?> blockBreakerT1BEClass;

    static {
        try {
            // Check if the class exists at runtime
            blockBreakerT1BEClass = Class.forName("com.direwolf20.justdirethings.common.blockentities.BlockBreakerT1BE");

        } catch (ClassNotFoundException e) {
            // If the class isn't present, we will not try to access it
            blockBreakerT1BEClass = null;
        }
    }

    //This is used to prevent the level writer from destroying the block in instances of the just dirt things block breaker

    @Inject(method = "breakBlock", at = @At(value = "HEAD"), cancellable = true)
    public void breakBlock(FakePlayer player, BlockPos breakPos, ItemStack itemStack, BlockState state, CallbackInfo ci) {
        Block block = state.getBlock();

        if (blockBreakerT1BEClass != null && blockBreakerT1BEClass.isInstance(this)) {
            try {

                Level level = player.level();

                if (breakPos != null) {
                    BlockEntity blockEntity = level.getBlockEntity(breakPos);

                    if (block instanceof UnbreakableResourceBlock unbreakableResourceBlock) {
                        int dropHeightModifier = unbreakableResourceBlock.dropHeightModifier;
                        BlockPos modifiedBlockPos = new BlockPos(breakPos.getX(), breakPos.getY() + dropHeightModifier, breakPos.getZ());

                        Block.dropResources(state, level, modifiedBlockPos, blockEntity, player, itemStack);

                        level.setBlock(breakPos, state.setValue(RESTING, true), Block.UPDATE_ALL);
                        level.scheduleTick(breakPos, state.getBlock(), 20);

                        if (state.getDestroySpeed(player.level(), breakPos) != 0.0F) {
                            itemStack.hurtAndBreak(1, player, LivingEntity.getSlotForHand(InteractionHand.MAIN_HAND));
                        }

                        ci.cancel();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //This is used to prevent the level writer from destroying the block in instances of the just dirt things block breaker
    @Inject(
            method = "tryBreakBlock(Lnet/minecraft/world/item/ItemStack;Lnet/neoforged/neoforge/common/util/FakePlayer;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;)Z",
            at = @At("HEAD"),
            cancellable = true
    )

    private void onTryBreakBlock(ItemStack tool, FakePlayer fakePlayer, BlockPos breakPos, BlockState state, CallbackInfoReturnable<Boolean> cir) {
        if (state.getBlock() instanceof UnbreakableResourceBlock && state.getValue(UnbreakableResourceBlock.RESTING)) {
            cir.setReturnValue(false);
        }
    }
}
