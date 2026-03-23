package com.benbenlaw.core.screen.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.DefaultTooltipPositioner;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.block.FluidModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.sprite.AtlasManager;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.client.extensions.common.ClientExtensionsManager;
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
    public static void renderFluid(GuiGraphicsExtractor guiGraphics, FluidStacksResourceHandler handler, int slot, int screenX, int screenY,
                                   int tankTopX, int tankTopY, int tankHeight, int tankWidth, int mouseX, int mouseY) {


        FluidStack fluidStack = FluidUtil.getStack(handler, slot);
        int capacity = handler.getCapacityAsInt(slot, FluidResource.of(fluidStack));

        int tankX = screenX + tankTopX;
        int tankY = screenY + tankTopY;

        if (!fluidStack.isEmpty()) {
            int displayLevel = (int) ((float) fluidStack.getAmount() / capacity * tankHeight);

            FluidModel fluidModel = Minecraft.getInstance().getModelManager().getFluidStateModelSet().get(fluidStack.getFluid().defaultFluidState());
            TextureAtlasSprite fluidSprite = fluidModel.stillMaterial().sprite();
            assert fluidModel.fluidTintSource() != null;
            renderTiledSprite(guiGraphics, fluidSprite, fluidModel.fluidTintSource().color(fluidStack.getFluid().defaultFluidState()),
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

            guiGraphics.tooltip(Minecraft.getInstance().font, tooltipComponents, mouseX, mouseY, DefaultTooltipPositioner.INSTANCE, null);


        }
    }

    public static void renderTiledSprite(GuiGraphicsExtractor guiGraphics, TextureAtlasSprite sprite, int color, int x, int y, int height, int width) {
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

    public static void renderFluidStack(GuiGraphicsExtractor guiGraphics, FluidStack fluid, int x, int y, int width, int height, int mouseX, int mouseY) {
        if (fluid.isEmpty()) return;

        FluidModel fluidModel = Minecraft.getInstance().getModelManager().getFluidStateModelSet().get(fluid.getFluid().defaultFluidState());
        TextureAtlasSprite fluidSprite = fluidModel.stillMaterial().sprite();
        assert fluidModel.fluidTintSource() != null;

        renderTiledSprite(guiGraphics, fluidSprite, fluidModel.fluidTintSource().color(fluid.getFluid().defaultFluidState()), x, y, height, width);
    }

    public static void renderFluidStackTooltip(GuiGraphicsExtractor guiGraphics, FluidStack fluid, FluidStacksResourceHandler handler, int slot, int x, int y, int width, int height, int mouseX, int mouseY) {
        if (!fluid.isEmpty()) {
            if (mouseX >= x && mouseX < x + width && mouseY >= y && mouseY < y + height) {

                int capacity = handler.getCapacityAsInt(slot, FluidResource.of(fluid));

                List<Component> lines = new ArrayList();
                lines.add(fluid.getHoverName());
                lines.add(Component.literal(String.format("%d / %d mB", fluid.getAmount(), capacity)));

                List<ClientTooltipComponent> tooltipComponents = lines.stream()
                        .map(Component::getVisualOrderText)
                        .map(ClientTooltipComponent::create)
                        .toList();

                guiGraphics.tooltip(
                        Minecraft.getInstance().font,
                        tooltipComponents,
                        mouseX,
                        mouseY,
                        DefaultTooltipPositioner.INSTANCE,
                        null
                );
            }
        }
    }
}
