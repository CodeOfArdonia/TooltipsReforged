package com.iafenvoy.tooltipsreforged.render;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.DiffuseLighting;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.CodEntity;
import net.minecraft.entity.passive.SalmonEntity;
import net.minecraft.entity.passive.SchoolingFishEntity;
import net.minecraft.item.ItemStack;
import org.joml.Matrix4f;
import org.joml.Quaternionf;

public interface RenderHelper {
    int SPACING = 4, SHADOW_LIGHT_COLOR = 15728880;

    default void drawStack(DrawContext context, ItemStack stack, int x, int y, float size) {
        if (stack.isEmpty()) return;
        MinecraftClient client = MinecraftClient.getInstance();
        BakedModel bakedModel = client.getItemRenderer().getModel(stack, client.world, client.player, 0);
        MatrixStack matrices = context.getMatrices();
        matrices.push();
        matrices.translate(x + size / 2, y + size / 2, 150);
        matrices.multiplyPositionMatrix((new Matrix4f()).scaling(1.0F, -1.0F, 1.0F));
        matrices.scale(size, size, size);
        boolean bl = !bakedModel.isSideLit();
        if (bl) DiffuseLighting.disableGuiDepthLighting();
        client.getItemRenderer().renderItem(stack, ModelTransformationMode.GUI, false, matrices, context.getVertexConsumers(), 15728880, OverlayTexture.DEFAULT_UV, bakedModel);
        context.draw();
        if (bl) DiffuseLighting.enableGuiDepthLighting();
        matrices.pop();
    }

    default void drawEntity(DrawContext context, int centerX, int centerY, int maxSize, float rotationYaw, LivingEntity entity) {
        float pitch = entity.getPitch(), yaw = entity.getYaw(), bodyYaw = entity.bodyYaw, headYaw = entity.headYaw;
        entity.setPitch(0);
        entity.setYaw(rotationYaw);
        entity.bodyYaw = rotationYaw;
        entity.headYaw = rotationYaw;

        double boxX = entity.getBoundingBox().getLengthX(), boxY = entity.getBoundingBox().getLengthY(), scale = (boxX + boxY) / 2;
        Quaternionf correctionRotation = entity instanceof CodEntity || entity instanceof SalmonEntity ? new Quaternionf().rotateZ((float) Math.toRadians(-90)) : new Quaternionf().rotateX((float) Math.toRadians(180));
        Quaternionf combinedRotation = new Quaternionf().rotateY((float) Math.toRadians(rotationYaw)).mul(correctionRotation);
        if (entity instanceof SchoolingFishEntity) {
            centerY -= 20;
            scale *= 1.5;
        }
        this.drawEntity(context, centerX - boxX / 2, centerY - boxY / 2, (float) (maxSize / scale), combinedRotation, entity);

        entity.setPitch(pitch);
        entity.setYaw(yaw);
        entity.bodyYaw = bodyYaw;
        entity.headYaw = headYaw;
    }

    default void drawEntity(DrawContext context, double x, double y, float size, Quaternionf rotation, Entity entity) {
        MatrixStack matrices = context.getMatrices();
        matrices.push();
        matrices.translate(x, y, 450);
        matrices.scale(-1, 1, 1);
        matrices.multiplyPositionMatrix(new Matrix4f().scaling(size, size, size));
        matrices.multiply(rotation);

        DiffuseLighting.method_34742();

        EntityRenderDispatcher dispatcher = MinecraftClient.getInstance().getEntityRenderDispatcher();
        dispatcher.setRenderShadows(false);
        dispatcher.render(entity, 0, 0, 0, 0, 1, context.getMatrices(), context.getVertexConsumers(), SHADOW_LIGHT_COLOR);
        dispatcher.setRenderShadows(true);

        matrices.pop();
        DiffuseLighting.enableGuiDepthLighting();
    }
}
