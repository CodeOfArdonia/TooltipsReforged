package com.iafenvoy.tooltipsreforged.component;

import com.iafenvoy.tooltipsreforged.Static;
import com.iafenvoy.tooltipsreforged.render.TooltipProviders;
import com.iafenvoy.tooltipsreforged.render.TooltipsRenderHelper;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Rarity;

import java.util.Objects;

@Environment(EnvType.CLIENT)
public class ColorBorderComponent extends BackgroundComponent {
    private final ItemStack stack;

    public ColorBorderComponent(ItemStack stack) {
        this.stack = stack;
    }

    @Override
    protected void renderBorder(DrawContext context, int x, int y, int width, int height, int z, int page) {
        int stackColor = TooltipProviders.getItemBorderColor(this.stack);
        int startColor = 0xff000000 | stackColor;
        if (stackColor == -1 || stackColor == 0)
            startColor = Objects.requireNonNullElse(Rarity.COMMON.formatting.getColorValue(), -1);
        int endColor = Static.END_COLOR;

        TooltipsRenderHelper.renderVerticalLine(context, x, y, height - 2, z, startColor, endColor);
        TooltipsRenderHelper.renderVerticalLine(context, x + width - 1, y, height - 2, z, startColor, endColor);
        TooltipsRenderHelper.renderHorizontalLine(context, x, y - 1, width, z, startColor);
        TooltipsRenderHelper.renderHorizontalLine(context, x, y - 1 + height - 1, width, z, endColor);
    }
}
