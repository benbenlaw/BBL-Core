package com.benbenlaw.core.item;

import com.benbenlaw.core.util.DirectionUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LightBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.Consumer;

public class LightItem extends Item {
    public LightItem(Properties properties) {
        super(properties.component(CoreDataComponents.LIGHT_LEVEL, 15));
    }

    /**
     * This can be used to place the vanilla light block in world as well as remove it
     * The light level is displayed in world when holding the item
     * If item is created with a durability, it will be reduced by 1 each time the light block is placed/removed
     * If the item is not damageable, it will not be consumed
     * Shift Scrolling will change the light level of the item
     * This item will be in BBL Colors 1.21.5+ to replace the Glowstone Spray Can behavior to this
     * LightBlockEmitLightEvent is the class for client side particle and text rendering when using this item
     * Networking is handlder by LightItemPayload and LightItemPacket to sync the changing of light level
     *
     * <p>
     *    Example usage in a mod's item registry:
     *    public static final DeferredItem<Item> LIGHT_ITEM = ITEMS.registerItem(
     *             "light_item",
     *             LightItem::new,
     *             new Item.Properties().durability(10)
     *     );
     * */

    @Override
    public @NotNull InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Direction direction = context.getClickedFace();
        BlockPos lightPos = pos.relative(direction);
        InteractionHand hand = context.getHand();
        Player player = context.getPlayer();

        if (player == null) return InteractionResult.PASS;

        ItemStack litItem = player.getItemInHand(hand);
        int lightLevel = litItem.getOrDefault(CoreDataComponents.LIGHT_LEVEL.get(), 15);
        EquipmentSlot slot = hand == InteractionHand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND;

        if (!level.isClientSide()) {
            BlockState current = level.getBlockState(lightPos);

            if (current.is(Blocks.LIGHT)) {
                level.removeBlock(lightPos, false);
            } else {
                level.setBlockAndUpdate(lightPos, Blocks.LIGHT.defaultBlockState().setValue(LightBlock.LEVEL, lightLevel));
                if (litItem.isDamageableItem()) {
                    litItem.hurtAndBreak(1, player, slot);
                } else {
                    litItem.shrink(1);
                }
            }
        }

        return InteractionResult.SUCCESS;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay tooltipDisplay, Consumer<Component> componentConsumer, TooltipFlag flag) {

        int lightLevel = stack.getOrDefault(CoreDataComponents.LIGHT_LEVEL.get(), 15);


        TooltipUtil.addShiftTooltip(tooltipDisplay, componentConsumer, Component.translatable("tooltip.core.light_level", lightLevel));

        //componentConsumer.accept(Component.translatable("tooltip.core.light_level", lightLevel));
    }
}
