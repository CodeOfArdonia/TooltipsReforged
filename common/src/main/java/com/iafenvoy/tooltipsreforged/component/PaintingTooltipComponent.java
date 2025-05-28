package com.iafenvoy.tooltipsreforged.component;

import com.iafenvoy.tooltipsreforged.config.TooltipReforgedConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.client.texture.PaintingManager;
import net.minecraft.client.texture.Sprite;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.decoration.painting.PaintingEntity;
import net.minecraft.entity.decoration.painting.PaintingVariant;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;

public class PaintingTooltipComponent implements TooltipComponent {
    private final PaintingVariant variant;
    private final int width;
    private final int height;
    private final TooltipReforgedConfig config = TooltipReforgedConfig.INSTANCE;

    public PaintingTooltipComponent(ItemStack stack) {
        NbtCompound nbtCompound = stack.getSubNbt("EntityTag");
        PaintingEntity painting = EntityType.PAINTING.create(MinecraftClient.getInstance().world);
        if (nbtCompound != null && painting != null) {
            painting.readNbt(nbtCompound.copy());
            this.variant = painting.getVariant().value();
            this.width = this.variant.getWidth() * 2;
            this.height = this.variant.getHeight() * 2;
        } else {
            this.variant = null;
            this.width = 0;
            this.height = 0;
        }
    }

    @Override
    public int getHeight() {
        return this.config.common.paintingTooltip.getValue() ? this.height : 0;
    }

    @Override
    public int getWidth(TextRenderer textRenderer) {
        return this.config.common.paintingTooltip.getValue() ? this.width : 0;
    }

    @Override
    public void drawItems(TextRenderer textRenderer, int x, int y, DrawContext context) {
        if (this.variant == null || !this.config.common.paintingTooltip.getValue()) return;
        PaintingManager paintingManager = MinecraftClient.getInstance().getPaintingManager();
        Sprite sprite = paintingManager.getPaintingSprite(this.variant);
        context.drawSprite(x, y, 0, this.width, this.height, sprite);
    }
}
