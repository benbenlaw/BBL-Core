package com.benbenlaw.core.screen.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.DefaultTooltipPositioner;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.block.FluidModel;
import net.minecraft.client.renderer.block.FluidStateModelSet;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.client.resources.model.sprite.AtlasManager;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.client.extensions.common.ClientExtensionsManager;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.client.fluid.FluidTintSource;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.fluid.FluidStacksResourceHandler;
import net.neoforged.neoforge.transfer.fluid.FluidUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FluidRenderingUtils {

    /// Used to render a fluid tank in a GUI use
    /// renderFluid(guiGraphics, tank, x, y, 8, 20, 47, 16, mouseX, mouseY);
    /// Replaces all previous screen fluid rendering code
    ///
    @Deprecated(forRemoval = true)
    public static void renderFluid(GuiGraphicsExtractor guiGraphics, FluidStacksResourceHandler handler, int slot, int screenX, int screenY,
                                   int tankTopX, int tankTopY, int tankHeight, int tankWidth, int mouseX, int mouseY) {


        FluidStack fluidStack = FluidUtil.getStack(handler, slot);
        int capacity = handler.getCapacityAsInt(slot, FluidResource.EMPTY);

        int tankX = screenX + tankTopX;
        int tankY = screenY + tankTopY;

        if (!fluidStack.isEmpty()) {
            int displayLevel = capacity > 0
                    ? (int)((float)fluidStack.getAmount() / capacity * tankHeight)
                    : 0;

            if (getStillFluidSprite(fluidStack).isPresent()) {
                renderTiledSprite(guiGraphics, getStillFluidSprite(fluidStack).get(), getColorTint(fluidStack),
                        tankX, tankY + tankHeight - displayLevel, displayLevel, tankWidth);
            }
        }

        if (mouseX >= tankX && mouseX < tankX + tankWidth &&
                mouseY >= tankY && mouseY < tankY + tankHeight) {

            List<Component> lines = new ArrayList<>();

            if (fluidStack.isEmpty()) {
                lines.add(Component.literal("Empty Filter"));
            } else {
                lines.add(fluidStack.getHoverName()); // fluid name
                lines.add(Component.literal(String.format("%d / %d mB", fluidStack.getAmount(), capacity))); // amount
            }

            guiGraphics.setTooltipForNextFrame(Minecraft.getInstance().font, lines, Optional.empty(), mouseX, mouseY);
        }
    }

    public static void renderFluid(GuiGraphicsExtractor guiGraphics, FluidStacksResourceHandler handler, int slot, int screenX, int screenY,
                                   int tankTopX, int tankTopY, int tankHeight, int tankWidth, int mouseX, int mouseY, Component emptyTooltip) {


        FluidStack fluidStack = FluidUtil.getStack(handler, slot);
        int capacity = handler.getCapacityAsInt(slot, FluidResource.EMPTY);

        int tankX = screenX + tankTopX;
        int tankY = screenY + tankTopY;

        if (!fluidStack.isEmpty()) {
            int displayLevel = capacity > 0
                    ? (int)((float)fluidStack.getAmount() / capacity * tankHeight)
                    : 0;

            if (getStillFluidSprite(fluidStack).isPresent()) {
                renderTiledSprite(guiGraphics, getStillFluidSprite(fluidStack).get(), getColorTint(fluidStack),
                        tankX, tankY + tankHeight - displayLevel, displayLevel, tankWidth);
            }
        }

        if (mouseX >= tankX && mouseX < tankX + tankWidth &&
                mouseY >= tankY && mouseY < tankY + tankHeight) {

            List<Component> lines = new ArrayList<>();

            if (fluidStack.isEmpty()) {
                lines.add(emptyTooltip);
            } else {
                lines.add(fluidStack.getHoverName()); // fluid name
                lines.add(Component.literal(String.format("%d / %d mB", fluidStack.getAmount(), capacity))); // amount
            }

            List<ClientTooltipComponent> tooltipComponents =
                    lines.stream().map(Component::getVisualOrderText)
                            .map(ClientTooltipComponent::create)
                            .toList();
            guiGraphics.setTooltipForNextFrame(Minecraft.getInstance().font, lines, Optional.empty(), mouseX, mouseY);


        }
    }

    public static void renderTiledSprite(GuiGraphicsExtractor guiGraphics, TextureAtlasSprite sprite, int color, int x, int y, int height, int width) {
        int spriteWidth = sprite.contents().width();
        int spriteHeight = sprite.contents().height();

        int textureWidth = (int)((float)sprite.contents().width() / (sprite.getU1() - sprite.getU0()));
        int textureHeight = (int)((float)sprite.contents().height() / (sprite.getV1() - sprite.getV0()));

        for (int currentX = 0; currentX < width; currentX += spriteWidth) {
            int renderWidth = Math.min(spriteWidth, width - currentX);

            for (int currentY = 0; currentY < height; currentY += spriteHeight) {
                int renderHeight = Math.min(spriteHeight, height - currentY);

                guiGraphics.blit(
                        RenderPipelines.GUI_TEXTURED,
                        sprite.atlasLocation(),
                        x + currentX,
                        y + currentY,
                        (float)textureWidth * sprite.getU0(),
                        (float)textureHeight * sprite.getV0(),
                        renderWidth,
                        renderHeight,
                        textureWidth,
                        textureHeight,
                        color
                );
            }
        }
    }

    public static void renderFluidStack(GuiGraphicsExtractor guiGraphics, FluidStack fluidStack, int x, int y, int width, int height, int mouseX, int mouseY) {
        if (fluidStack.isEmpty()) return;
        if (getStillFluidSprite(fluidStack).isPresent()) {
            renderTiledSprite(guiGraphics, getStillFluidSprite(fluidStack).get(), getColorTint(fluidStack), x, y, height, width);
        }
    }

    public static void renderFluidStackTooltip(GuiGraphicsExtractor guiGraphics, FluidStack fluid, FluidStacksResourceHandler handler, int slot, int x, int y, int width, int height, int mouseX, int mouseY) {
        if (!fluid.isEmpty()) {
            if (mouseX >= x && mouseX < x + width && mouseY >= y && mouseY < y + height) {

                int capacity = handler.getCapacityAsInt(slot, FluidResource.of(fluid));

                List<Component> lines = new ArrayList();
                lines.add(fluid.getHoverName());
                lines.add(Component.literal(String.format("%d / %d mB", fluid.getAmount(), capacity)));

                guiGraphics.setTooltipForNextFrame(Minecraft.getInstance().font, lines, Optional.empty(), mouseX, mouseY);
            }
        }
    }

    public static int getColorTint(FluidStack ingredient) {
        Fluid fluid = ingredient.getFluid();
        Minecraft minecraft = Minecraft.getInstance();
        ModelManager modelManager = minecraft.getModelManager();
        FluidStateModelSet fluidStateModelSet = modelManager.getFluidStateModelSet();
        FluidModel fluidModel = fluidStateModelSet.get(fluid.defaultFluidState());
        FluidTintSource tintSource = fluidModel.fluidTintSource();
        if (tintSource == null) {
            return 0xFFFFFFFF;
        }
        return tintSource.colorAsStack(ingredient);
    }

    public static Optional<TextureAtlasSprite> getStillFluidSprite(FluidStack fluidStack) {
        Fluid fluid = fluidStack.getFluid();
        Minecraft minecraft = Minecraft.getInstance();
        ModelManager modelManager = minecraft.getModelManager();
        FluidStateModelSet fluidStateModelSet = modelManager.getFluidStateModelSet();
        FluidModel fluidModel = fluidStateModelSet.get(fluid.defaultFluidState());
        Material.Baked stillMaterial = fluidModel.stillMaterial();
        TextureAtlasSprite sprite = stillMaterial.sprite();
        // noinspection OptionalOfNullableMisuse
        return Optional.ofNullable(sprite)
                .filter(s -> s.atlasLocation() != MissingTextureAtlasSprite.getLocation());
    }
}