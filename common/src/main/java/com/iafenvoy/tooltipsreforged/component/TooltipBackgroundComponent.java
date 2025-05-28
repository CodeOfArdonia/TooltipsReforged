package com.iafenvoy.tooltipsreforged.component;

import com.iafenvoy.tooltipsreforged.Static;
import com.iafenvoy.tooltipsreforged.config.TooltipReforgedConfig;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.util.Rarity;

import java.util.Objects;

public class TooltipBackgroundComponent implements TooltipComponent {
    protected static final int INNER_PADDING = 4;

    public void render(DrawContext context, int x, int y, int width, int height, int z, int page) throws Exception {
        int i = x - INNER_PADDING;
        int j = y - INNER_PADDING;
        int k = width + INNER_PADDING * 2;
        int l = height + INNER_PADDING * 2;

        int bgColor = TooltipReforgedConfig.INSTANCE.backgroundColor.getRGB();

        this.renderHorizontalLine(context, i, j - 1, k, z, bgColor);
        this.renderHorizontalLine(context, i, j + l, k, z, bgColor);
        this.renderRectangle(context, i, j, k, l, z);
        this.renderVerticalLine(context, i - 1, j, l, z, bgColor, bgColor);
        this.renderVerticalLine(context, i + k, j, l, z, bgColor, bgColor);
        this.renderBorder(context, i, j + 1, k, l, z, page);
    }

    protected void renderBorder(DrawContext context, int x, int y, int width, int height, int z, int page) {
        int startColor = Objects.requireNonNullElse(Rarity.COMMON.formatting.getColorValue(), -1);
        int endColor = Static.END_COLOR;

        this.renderVerticalLine(context, x, y, height - 2, z, startColor, endColor);
        this.renderVerticalLine(context, x + width - 1, y, height - 2, z, startColor, endColor);
        this.renderHorizontalLine(context, x, y - 1, width, z, startColor);
        this.renderHorizontalLine(context, x, y - 1 + height - 1, width, z, endColor);
    }

    protected void renderVerticalLine(DrawContext context, int x, int y, int height, int z, int startColor, int endColor) {
        context.fillGradient(x, y, x + 1, y + height, z, startColor, endColor);
    }

    protected void renderHorizontalLine(DrawContext context, int x, int y, int width, int z, int color) {
        context.fill(x, y, x + width, y + 1, z, color);
    }

    protected void renderRectangle(DrawContext context, int x, int y, int width, int height, int z) {
        context.fill(x, y, x + width, y + height, z, TooltipReforgedConfig.INSTANCE.backgroundColor.getRGB());
    }

    @Override
    public int getHeight() {
        return 0;
    }

    @Override
    public int getWidth(TextRenderer textRenderer) {
        return 0;
    }
}
