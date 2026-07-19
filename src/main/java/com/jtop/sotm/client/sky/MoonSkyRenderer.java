package com.jtop.sotm.client.sky;

import com.jtop.sotm.moon.MoonPhase;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.MeshData;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import org.joml.Matrix4f;
import org.joml.Matrix4fStack;

@EventBusSubscriber(value = Dist.CLIENT)
public class MoonSkyRenderer {

    private static boolean initialized = false;

    public static void init() {
        initialized = true;
    }

    @SubscribeEvent
    public static void onRenderLevelStage(RenderLevelStageEvent event) {
        if (!initialized) return;
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_SKY) return;

        Minecraft mc = Minecraft.getInstance();
        ClientLevel level = mc.level;
        if (level == null) return;
        if (level.dimension() != Level.OVERWORLD) return;
        if (level.isDay()) return;

        MoonPhase phase = MoonTextureManager.getCurrentPhase();
        if (phase == MoonPhase.NORMAL || phase == MoonPhase.CALM || phase == MoonPhase.MISSING) return;

        float partialTick = event.getPartialTick().getGameTimeDeltaPartialTick(false);
        renderCustomMoon(level, phase, partialTick);
    }

    private static void renderCustomMoon(ClientLevel level, MoonPhase phase, float partialTick) {
        Matrix4fStack modelViewStack = RenderSystem.getModelViewStack();
        modelViewStack.pushMatrix();

        modelViewStack.rotateY((float) Math.toRadians(-90.0));
        modelViewStack.rotateZ((float) Math.toRadians(level.getTimeOfDay(partialTick) * 360.0));
        modelViewStack.rotateZ((float) Math.toRadians(180.0));

        RenderSystem.applyModelViewMatrix();

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, MoonTextureManager.getCurrentMoonTexture());
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);

        // Dibujar quad con matriz identidad (la transformación va por model-view)
        BufferBuilder buffer = Tesselator.getInstance().begin(
                VertexFormat.Mode.QUADS,
                DefaultVertexFormat.POSITION_TEX
        );

        Matrix4f identity = new Matrix4f().identity();
        float size = 15.0f;
        float y = -100.0f;

        buffer.addVertex(identity, -size, y, size).setUv(0.0f, 0.0f);
        buffer.addVertex(identity, size, y, size).setUv(1.0f, 0.0f);
        buffer.addVertex(identity, size, y, -size).setUv(1.0f, 1.0f);
        buffer.addVertex(identity, -size, y, -size).setUv(0.0f, 1.0f);

        MeshData meshData = buffer.build();
        BufferUploader.drawWithShader(meshData);

        modelViewStack.popMatrix();
        RenderSystem.applyModelViewMatrix();

        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.disableBlend();
    }
}