package com.benbenlaw.core.screen.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.DefaultTooltipPositioner;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.AtlasManager;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.fluid.FluidStacksResourceHandler;
import net.neoforged.neoforge.transfer.fluid.FluidUtil;

import java.util.ArrayList;
import java.util.List;

public class FluidRenderingUtils {

    /// Used to render a fluid tank in a GUI use
    /// renderFluid(guiGraphics, tank, x, y, 8, 20, 47, 16, mouseX, mouseY);
    /// Replaces all previous screen fluid rendering code
    public static void renderFluid(GuiGraphics guiGraphics, FluidStacksResourceHandler handler, int slot, int screenX, int screenY,
                                   int tankTopX, int tankTopY, int tankHeight, int tankWidth, int mouseX, int mouseY) {


        FluidStack fluidStack = FluidUtil.getStack(handler, slot);
        int capacity = handler.getCapacityAsInt(slot, FluidResource.of(fluidStack));

        int tankX = screenX + tankTopX;
        int tankY = screenY + tankTopY;

        if (!fluidStack.isEmpty()) {
            int displayLevel = (int) ((float) fluidStack.getAmount() / capacity * tankHeight);

            IClientFluidTypeExtensions renderProperties = IClientFluidTypeExtensions.of(fluidStack.getFluid());
            ResourceLocation texture = renderProperties.getStillTexture(fluidStack);
            AtlasManager atlas = Minecraft.getInstance().getAtlasManager();
            TextureAtlasSprite still = atlas.getAtlasOrThrow(ResourceLocation.withDefaultNamespace("blocks")).getSprite(texture);
            renderTiledSprite(guiGraphics, still, renderProperties.getTintColor(fluidStack),
                    tankX, tankY + tankHeight - displayLevel, displayLevel, tankWidth);
        }

        if (mouseX >= tankX && mouseX < tankX + tankWidth &&
                mouseY >= tankY && mouseY < tankY + tankHeight) {

            List<Component> lines = new ArrayList<>();

            if (fluidStack.isEmpty()) {
                lines.add(Component.literal("Empty"));
            } else {
                lines.add(fluidStack.getHoverName()); // fluid name
                lines.add(Component.literal(String.format("%d / %d mB", fluidStack.getAmount(), capacity))); // amount
            }

            List<ClientTooltipComponent> tooltipComponents =
                    lines.stream().map(Component::getVisualOrderText)
                            .map(ClientTooltipComponent::create)
                            .toList();

            guiGraphics.renderTooltip(Minecraft.getInstance().font, tooltipComponents, mouseX, mouseY, DefaultTooltipPositioner.INSTANCE, null);


        }
    }

    public static void renderTiledSprite(GuiGraphics guiGraphics, TextureAtlasSprite sprite, int color, int x, int y, int height, int width) {
        int spriteHeight = sprite.contents().height();
        int startY = y;
        int textureWidth = (int) (sprite.contents().width() / (sprite.getU1() - sprite.getU0()));
        int textureHeight = (int) (sprite.contents().height() / (sprite.getV1() - sprite.getV0()));
        do {
            int renderHeight = Math.min(spriteHeight, height);
            height -= renderHeight;

            guiGraphics.blit(RenderPipelines.GUI_TEXTURED, sprite.atlasLocation(), x, startY, textureWidth * sprite.getU0(), textureHeight * sprite.getV0(), width, renderHeight, textureWidth, textureHeight, color);

            startY += renderHeight;
        } while (height > 0);
    }

    public static void renderFluidStack(GuiGraphics guiGraphics, FluidStack fluid, int x, int y, int width, int height, int mouseX, int mouseY) {
        if (fluid.isEmpty()) return;

        IClientFluidTypeExtensions renderProperties = IClientFluidTypeExtensions.of(fluid.getFluid());
        ResourceLocation texture = renderProperties.getStillTexture(fluid);
        AtlasManager atlas = Minecraft.getInstance().getAtlasManager();
        TextureAtlasSprite still = atlas.getAtlasOrThrow(ResourceLocation.withDefaultNamespace("blocks")).getSprite(texture);

        renderTiledSprite(guiGraphics, still, renderProperties.getTintColor(fluid), x, y, height, width);
    }

    public static void renderFluidStackTooltip(GuiGraphics guiGraphics, FluidStack fluid, int x, int y, int width, int height, int mouseX, int mouseY) {
        if (fluid.isEmpty()) return;

        if (mouseX >= x && mouseX < x + width && mouseY >= y && mouseY < y + height) {
            List<Component> lines = new ArrayList<>();
            lines.add(fluid.getHoverName());
            lines.add(Component.literal(String.format("%d mB", fluid.getAmount())));

            List<ClientTooltipComponent> tooltipComponents =
                    lines.stream().map(Component::getVisualOrderText)
                            .map(ClientTooltipComponent::create)
                            .toList();

            guiGraphics.renderTooltip(Minecraft.getInstance().font, tooltipComponents, mouseX, mouseY, DefaultTooltipPositioner.INSTANCE, null);
        }
    }
}
