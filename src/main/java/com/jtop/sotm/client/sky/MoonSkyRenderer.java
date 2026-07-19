package com.jtop.sotm.client.sky;

import com.jtop.sotm.moon.MoonPhase;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.MeshData;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import org.joml.Matrix4f;

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

        float partialTick = event.getPartialTick().getGameTimeDeltaPartialTick(false);
        float timeOfDay = level.getTimeOfDay(partialTick);
        boolean isNight = timeOfDay > 0.25f && timeOfDay < 0.75f;
        if (!isNight) return;

        MoonPhase phase = MoonTextureManager.getCurrentPhase();
        if (phase == MoonPhase.NORMAL || phase == MoonPhase.CALM || phase == MoonPhase.MISSING) return;

        renderCustomMoon(event.getPoseStack(), mc, phase, partialTick);
    }

    private static void renderCustomMoon(PoseStack poseStack, Minecraft mc, MoonPhase phase, float partialTick) {
        poseStack.pushPose();

        ClientLevel level = mc.level;

        poseStack.mulPose(Axis.YP.rotationDegrees(-90.0F));
        poseStack.mulPose(Axis.ZP.rotationDegrees(level.getTimeOfDay(partialTick) * 360.0F));
        poseStack.mulPose(Axis.ZP.rotationDegrees(180.0F));

        Matrix4f matrix = poseStack.last().pose();

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, MoonTextureManager.getCurrentMoonTexture());
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);

        BufferBuilder buffer = Tesselator.getInstance().begin(
                VertexFormat.Mode.QUADS,
                DefaultVertexFormat.POSITION_TEX
        );

        float size = 20.0f;
        float y = 100.0f;

        buffer.addVertex(matrix, -size, y, -size).setUv(0.0f, 1.0f);
        buffer.addVertex(matrix, size, y, -size).setUv(1.0f, 1.0f);
        buffer.addVertex(matrix, size, y, size).setUv(1.0f, 0.0f);
        buffer.addVertex(matrix, -size, y, size).setUv(0.0f, 0.0f);

        MeshData meshData = buffer.build();
        BufferUploader.drawWithShader(meshData);

        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.disableBlend();
        poseStack.popPose();
    }
}