package com.benbenlaw.core.util;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraft.util.ARGB;
import org.joml.Vector3f;

public class RenderUtil {

    // Render a face with default full brightness
    public static void renderFace(Direction face, PoseStack.Pose pose, VertexConsumer consumer,
                                  TextureAtlasSprite texture, float x, float y, float z, float w, float h, int color) {
        renderFace(face, pose, consumer, texture, x, y, z, w, h, color, LightTexture.FULL_BRIGHT);
    }

    // Render a face with specified light
    public static void renderFace(Direction face, PoseStack.Pose pose, VertexConsumer consumer,
                                  TextureAtlasSprite texture, float x, float y, float z, float w, float h, int color, int light) {
        switch (face) {
            case DOWN -> renderFace(pose, consumer, texture, color, light,
                    x, x + w, y, y, z, z, z, z, x, x + w, y, y, 0, -1, 0);
            case UP -> renderFace(pose, consumer, texture, color, light,
                    x, x + w, y + h, y + h, z, z, z, z, x, x + w, y, y, 0, 1, 0);
            case NORTH -> renderFace(pose, consumer, texture, color, light,
                    x, x + w, y + h, y, z, z, z, z, x, x + w, y, y + h, 0, 0, -1);
            case SOUTH -> renderFace(pose, consumer, texture, color, light,
                    x, x + w, y, y + h, z, z, z, z, x + w, x, y + h, y, 0, 0, 1);
            case EAST -> renderFace(pose, consumer, texture, color, light,
                    x, x, y + h, y, z, z + w, z + w, z, z, z + w, y, y + h, 1, 0, 0);
            case WEST -> renderFace(pose, consumer, texture, color, light,
                    x, x, y, y + h, z, z + w, z + w, z, x + w, x, y + h, y, -1, 0, 0);
        }
    }

    // Core face renderer
// Core face renderer
    private static void renderFace(PoseStack.Pose pose, VertexConsumer consumer, TextureAtlasSprite texture,
                                   int color, int light,
                                   float x0, float x1, float y0, float y1,
                                   float z0, float z1, float z2, float z3,
                                   float u0, float u1, float v0, float v1,
                                   float normalX, float normalY, float normalZ) {

        float minU = texture.getU(u0);
        float maxU = texture.getU(u1);
        float minV = texture.getV(v0);
        float maxV = texture.getV(v1);

        consumer.addVertex(x0, y0, z0)
                .setColor(color)
                .setUv(minU, minV)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(light)
                .setNormal(pose, normalX, normalY, normalZ);

        consumer.addVertex(x1, y0, z1)
                .setColor(color)
                .setUv(maxU, minV)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(light)
                .setNormal(pose, normalX, normalY, normalZ);

        consumer.addVertex(x1, y1, z2)
                .setColor(color)
                .setUv(maxU, maxV)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(light)
                .setNormal(pose, normalX, normalY, normalZ);

        consumer.addVertex(x0, y1, z3)
                .setColor(color)
                .setUv(minU, maxV)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(light)
                .setNormal(pose, normalX, normalY, normalZ);
    }


    // ARGB helpers
    public static float getRed(int color) {
        return (float) ARGB.red(color) / 255.0F;
    }

    public static float getGreen(int color) {
        return (float) ARGB.green(color) / 255.0F;
    }

    public static float getBlue(int color) {
        return (float) ARGB.blue(color) / 255.0F;
    }

    public static float getAlpha(int color) {
        return (float) ARGB.alpha(color) / 255.0F;
    }

    // Pack ARGB into int (optional)
    public static int packARGB(int alpha, int red, int green, int blue) {
        return (alpha & 0xFF) << 24 | (red & 0xFF) << 16 | (green & 0xFF) << 8 | (blue & 0xFF);
    }
}
