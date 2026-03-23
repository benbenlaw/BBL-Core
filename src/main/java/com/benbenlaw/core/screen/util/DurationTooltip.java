package com.benbenlaw.core.screen.util;

import com.benbenlaw.core.Core;
import com.benbenlaw.core.util.MouseUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.DefaultTooltipPositioner;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.FormattedCharSequence;

import java.util.List;

public class DurationTooltip {

    private static final Identifier TEXTURE = Core.identifier("duration_icon");

    public static void renderDurationTooltip(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, int x, int y, int xOffset, int yOffset, int progress, int totalDuration) {

        guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, TEXTURE, 10, 10, 0, 0, x + xOffset, y + yOffset, 10, 10);

        if (MouseUtil.isMouseAboveArea(mouseX, mouseY, x, y, xOffset, yOffset, 10, 10)) {
            Component progressTick = Component.translatable("tooltip.core.duration_tooltip", progress, totalDuration);
            FormattedCharSequence sequence = progressTick.getVisualOrderText();
            List<ClientTooltipComponent> tooltipLines = List.of(ClientTooltipComponent.create(sequence));
            guiGraphics.tooltip(Minecraft.getInstance().font, tooltipLines, mouseX, mouseY, DefaultTooltipPositioner.INSTANCE, null);

        }
    }
}
