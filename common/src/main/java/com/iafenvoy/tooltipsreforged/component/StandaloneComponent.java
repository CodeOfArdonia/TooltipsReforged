package com.iafenvoy.tooltipsreforged.component;

import com.iafenvoy.tooltipsreforged.render.ExtendedTooltipBackgroundRenderer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public abstract class StandaloneComponent implements TooltipComponent {
    protected final ItemStack stack;

    public StandaloneComponent(ItemStack stack) {
        this.stack = stack;
    }

    public abstract void render(DrawContext context, int x, int y, int z);

    protected void renderBackground(DrawContext context, int x, int y, int width, int height, int z) {
        ExtendedTooltipBackgroundRenderer.render(this.stack, context, x, y, width, height, z);
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
