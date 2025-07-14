package com.iafenvoy.tooltipsreforged.component;

import com.iafenvoy.tooltipsreforged.config.TooltipReforgedConfig;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.client.texture.Sprite;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.decoration.painting.PaintingEntity;
import net.minecraft.entity.decoration.painting.PaintingVariant;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;

@Environment(EnvType.CLIENT)
public class PaintingComponent implements TooltipComponent {
    private final PaintingVariant variant;
    private final int width, height;

    public PaintingComponent(ItemStack stack) {
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
        return TooltipReforgedConfig.INSTANCE.tooltip.paintingTooltip.getValue() ? this.height : 0;
    }

    @Override
    public int getWidth(TextRenderer textRenderer) {
        return TooltipReforgedConfig.INSTANCE.tooltip.paintingTooltip.getValue() ? this.width : 0;
    }

    @Override
    public void drawItems(TextRenderer textRenderer, int x, int y, DrawContext context) {
        if (this.variant == null || !TooltipReforgedConfig.INSTANCE.tooltip.paintingTooltip.getValue()) return;
        Sprite sprite = MinecraftClient.getInstance().getPaintingManager().getPaintingSprite(this.variant);
        context.drawSprite(x, y, 0, this.width, this.height, sprite);
    }
}
