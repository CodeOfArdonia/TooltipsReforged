package com.iafenvoy.tooltipsreforged.component;

import com.iafenvoy.tooltipsreforged.Static;
import com.iafenvoy.tooltipsreforged.config.TooltipReforgedConfig;
import com.iafenvoy.tooltipsreforged.render.TooltipsRenderHelper;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.util.Rarity;

import java.util.Objects;

@Environment(EnvType.CLIENT)
public class BackgroundComponent implements TooltipComponent {
    public void render(DrawContext context, int x, int y, int width, int height, int z, int page) throws Exception {
        int i = x - 4;
        int j = y - 4;
        int k = width + 8;
        int l = height + 8;
        int bgColor = TooltipReforgedConfig.INSTANCE.misc.backgroundColor.getValue();

        TooltipsRenderHelper.renderHorizontalLine(context, i, j - 1, k, z, bgColor);
        TooltipsRenderHelper.renderHorizontalLine(context, i, j + l, k, z, bgColor);
        TooltipsRenderHelper.renderRectangle(context, i, j, k, l, z);
        TooltipsRenderHelper.renderVerticalLine(context, i - 1, j, l, z, bgColor, bgColor);
        TooltipsRenderHelper.renderVerticalLine(context, i + k, j, l, z, bgColor, bgColor);
        this.renderBorder(context, i, j + 1, k, l, z, page);
    }

    protected void renderBorder(DrawContext context, int x, int y, int width, int height, int z, int page) {
        int startColor = Objects.requireNonNullElse(Rarity.COMMON.formatting.getColorValue(), -1);
        int endColor = Static.END_COLOR;

        TooltipsRenderHelper.renderVerticalLine(context, x, y, height - 2, z, startColor, endColor);
        TooltipsRenderHelper.renderVerticalLine(context, x + width - 1, y, height - 2, z, startColor, endColor);
        TooltipsRenderHelper.renderHorizontalLine(context, x, y - 1, width, z, startColor);
        TooltipsRenderHelper.renderHorizontalLine(context, x, y - 1 + height - 1, width, z, endColor);
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
