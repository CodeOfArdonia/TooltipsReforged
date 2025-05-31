package com.iafenvoy.tooltipsreforged.component;

import com.iafenvoy.tooltipsreforged.config.TooltipReforgedConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.MapRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.FilledMapItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.map.MapState;

public class MapComponent implements TooltipComponent {
    private final ItemStack stack;
    private final TooltipReforgedConfig config = TooltipReforgedConfig.INSTANCE;

    public MapComponent(ItemStack stack) {
        this.stack = stack;
    }

    @Override
    public int getHeight() {
        return this.config.common.mapTooltip.getValue() ? 130 : 0;
    }

    @Override
    public int getWidth(TextRenderer textRenderer) {
        return this.config.common.mapTooltip.getValue() ? 128 : 0;
    }

    @Override
    public void drawItems(TextRenderer textRenderer, int x, int y, DrawContext context) {
        if (!this.config.common.mapTooltip.getValue()) return;
        VertexConsumerProvider vertexConsumers = MinecraftClient.getInstance().getBufferBuilders().getEntityVertexConsumers();
        MapRenderer mapRenderer = MinecraftClient.getInstance().gameRenderer.getMapRenderer();

        Integer mapId = FilledMapItem.getMapId(this.stack);
        MapState mapState = FilledMapItem.getMapState(this.stack, MinecraftClient.getInstance().world);
        if (mapId == null || mapState == null) return;

        MatrixStack matrices = context.getMatrices();
        matrices.push();
        matrices.translate(x, y, 0);
        matrices.scale(1, 1, 0);
        mapRenderer.updateTexture(mapId, mapState);
        mapRenderer.draw(matrices, vertexConsumers, mapId, mapState, false, LightmapTextureManager.MAX_LIGHT_COORDINATE);
        matrices.pop();
    }
}
