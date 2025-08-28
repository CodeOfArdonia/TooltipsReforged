package com.iafenvoy.tooltipsreforged.render;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.DiffuseLighting;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import org.joml.Matrix4f;

public interface RenderHelper {
    int SPACING = 4;

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
}
