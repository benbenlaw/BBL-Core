package com.benbenlaw.core.screen.util.button;

import com.benbenlaw.core.Core;
import com.benbenlaw.core.block.entity.WhitelistBlockEntity;
import com.benbenlaw.core.network.packets.SyncWhitelistMode;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.DefaultTooltipPositioner;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;

import java.util.List;

public class WhitelistButton extends Button {

    private boolean whitelist;

    public WhitelistButton(int x, int y, int width, int height, boolean initial, OnPress onPress) {
        super(x, y, width, height, Component.empty(), onPress, DEFAULT_NARRATION);
        this.whitelist = initial;
        this.height = 18;
        this.width = 18;
    }

    public void toggle() {
        this.whitelist = !this.whitelist;
    }

    @Override
    protected void renderContents(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        boolean hovered = this.isHovered();
        Identifier currentTexture = this.whitelist
                ? (hovered ? Core.identifier("whitelist_button/whitelist_hover") : Core.identifier("whitelist_button/whitelist"))
                : (hovered ? Core.identifier("whitelist_button/blacklist_hover") : Core.identifier("whitelist_button/blacklist"));

        // Draw button background
        guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, currentTexture, this.getX(), this.getY(), this.width, this.height);

        // Render tooltip if hovered
        if (hovered) {
            Component modeText = Component.translatable(
                    this.whitelist
                            ? "tooltip.bblcore.whitelist_button.whitelist"
                            : "tooltip.bblcore.whitelist_button.blacklist"
            ).withStyle(this.whitelist ? net.minecraft.ChatFormatting.GREEN : net.minecraft.ChatFormatting.RED);

            Component tooltip = Component.translatable("tooltip.bblcore.whitelist_button.mode", modeText);

            List<ClientTooltipComponent> tooltipComponents = List.of(ClientTooltipComponent.create(tooltip.getVisualOrderText()));
            guiGraphics.renderTooltip(
                    Minecraft.getInstance().font,
                    tooltipComponents,
                    mouseX,
                    mouseY,
                    DefaultTooltipPositioner.INSTANCE,
                    null
            );
        }
    }


    public static WhitelistButton create(int x, int y, int width, int height, BlockEntity blockEntity) {

        if (blockEntity instanceof WhitelistBlockEntity whitelistBlockEntity) {

            boolean initialMode = whitelistBlockEntity.isWhitelist();

            return new WhitelistButton(x, y, width, height, initialMode, button -> {
                WhitelistButton whitelistButton = (WhitelistButton) button;
                whitelistButton.toggle();

                boolean newMode = !whitelistBlockEntity.isWhitelist();
                whitelistBlockEntity.setWhitelist(newMode);

                ClientPacketDistributor.sendToServer(new SyncWhitelistMode(blockEntity.getBlockPos(), newMode));
            });
        } else {
            Core.LOGGER.error("Attempted to create WhitelistButton for a BlockEntity that does not implement WhitelistBlockEntity: " + blockEntity);
            return null;
        }
    }
}
