package com.iafenvoy.tooltipsreforged.render;

import com.iafenvoy.tooltipsreforged.TooltipReforgedClient;
import com.iafenvoy.tooltipsreforged.component.StandaloneComponent;
import com.iafenvoy.tooltipsreforged.config.TooltipReforgedConfig;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.client.gui.tooltip.TooltipPositioner;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import org.joml.Vector2ic;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Environment(EnvType.CLIENT)
public class TooltipsRenderHelper {
    private static final int EDGE_SPACING = 32;
    private static final int PAGE_SPACING = 12;

    private static int getMaxHeight() {
        return MinecraftClient.getInstance().getWindow().getScaledHeight() - EDGE_SPACING * 2;
    }

    private static int getMaxWidth() {
        return MinecraftClient.getInstance().getWindow().getScaledWidth() / 2 - EDGE_SPACING;
    }

    public static void drawTooltip(ItemStack stack, DrawContext context, TextRenderer textRenderer, List<TooltipComponent> components, int x, int y, TooltipPositioner positioner) {
        if (components.isEmpty()) return;

        MatrixStack matrices = context.getMatrices();
        List<TooltipPage> pageList = new LinkedList<>();
        List<StandaloneComponent> standaloneComponents = new LinkedList<>();

        double scale = TooltipReforgedConfig.INSTANCE.misc.scaleFactor.getValue();

        int maxWidth = (int) (getMaxWidth() / scale);
        int totalWidth = 0;

        int pageHeight = -2;
        int maxHeight = (int) (getMaxHeight() / scale);

        int spacing = components.size() > 1 ? 4 : 0;
        pageHeight += spacing;

        TooltipPage page = new TooltipPage();

        for (TooltipComponent component : components) {
            if (component instanceof StandaloneComponent standalone) {
                standaloneComponents.add(standalone);
                continue;
            }
            int width = component.getWidth(textRenderer);
            int height = component.getHeight();

            if (width > maxWidth) {
                int wrappedWidth = component.getWidth(textRenderer);
                int wrappedHeight = component.getHeight();

                if (pageHeight + wrappedHeight > maxHeight) {
                    pageList.add(page);
                    totalWidth += page.width;
                    page = new TooltipPage();
                    pageHeight = -2;
                }

                page.components.add(component);
                page.height = pageHeight += wrappedHeight;
                page.width = Math.max(page.width, wrappedWidth);
            } else {
                if (pageHeight + height > maxHeight) {
                    pageList.add(page);
                    totalWidth += page.width;
                    page = new TooltipPage();
                    pageHeight = -2;
                }

                page.components.add(component);
                page.height = pageHeight += height;
                page.width = Math.max(page.width, width);
            }
        }

        if (!page.components.isEmpty()) {
            pageList.add(page);
            totalWidth += page.width;
        }

        int scaledOffset = ((int) (12 * TooltipReforgedConfig.INSTANCE.misc.scaleFactor.getValue())) - 12;
        Vector2ic vector2ic = positioner.getPosition(context.getScaledWindowWidth(), context.getScaledWindowHeight(), x + scaledOffset, y - scaledOffset, (int) (totalWidth * scale), (int) (pageList.get(0).height * scale));
        int n = vector2ic.x();
        int o = vector2ic.y();

        for (TooltipPage tooltipPage : pageList) {
            tooltipPage.x = n;
            tooltipPage.y = (pageList.size() > 1) ? o - EDGE_SPACING : o - 6;
            n += tooltipPage.width + PAGE_SPACING;
        }

        matrices.push();
        matrices.scale((float) scale, (float) scale, (float) scale);
        matrices.translate(0, 0, 400.0f);

        for (int i = 0; i < pageList.size(); i++) {
            TooltipPage p = pageList.get(i);
            ExtendedTooltipBackgroundRenderer.render(stack, context, p.x, p.y, p.width, p.height, 400);
            int cx = (int) (p.x / scale);
            int cy = (int) (p.y / scale);

            for (TooltipComponent component : p.components) {
                try {
                    component.drawText(textRenderer, cx, cy, matrices.peek().getPositionMatrix(), context.getVertexConsumers());
                    component.drawItems(textRenderer, cx, cy, context);
                    cy += component.getHeight();

                    if (i == 0 && component == p.components.get(0) && components.size() > 1)
                        cy += spacing;
                } catch (Exception e) {
                    TooltipReforgedClient.LOGGER.error("{}", TooltipReforgedClient.MOD_ID, e);
                }
            }
        }

        for (StandaloneComponent component : standaloneComponents)
            try {
                component.render(context, x, y, 0);
            } catch (Exception e) {
                TooltipReforgedClient.LOGGER.error("{}", TooltipReforgedClient.MOD_ID, e);
            }

        matrices.pop();
    }


    public static void drawNineSlicedTexture(DrawContext context, Identifier texture, int x, int y, int width, int height, int border, int textureWidth, int textureHeight) {
        context.drawTexture(texture, x, y, 0, 0, border, border, textureWidth, textureHeight);
        drawRepeatingTexture(context, texture, x + border, y, width - border * 2, border, border, 0, textureWidth - border * 2, border, textureWidth, textureHeight);
        context.drawTexture(texture, x + width - border, y, textureWidth - border, 0, border, border, textureWidth, textureHeight);

        drawRepeatingTexture(context, texture, x, y + border, border, height - border * 2, 0, border, border, textureHeight - border * 2, textureWidth, textureHeight);
        drawRepeatingTexture(context, texture, x + border, y + border, width - border * 2, height - border * 2, border, border, textureWidth - border * 2, textureHeight - border * 2, textureWidth, textureHeight);
        drawRepeatingTexture(context, texture, x + width - border, y + border, border, height - border * 2, textureWidth - border, border, border, textureHeight - border * 2, textureWidth, textureHeight);

        context.drawTexture(texture, x, y + height - border, 0, textureHeight - border, border, border, textureWidth, textureHeight);
        drawRepeatingTexture(context, texture, x + border, y + height - border, width - border * 2, border, border, textureHeight - border, textureWidth - border * 2, border, textureWidth, textureHeight);
        context.drawTexture(texture, x + width - border, y + height - border, textureWidth - border, textureHeight - border, border, border, textureWidth, textureHeight);
    }

    public static void drawRepeatingTexture(DrawContext context, Identifier texture, int x, int y, int width, int height, int u, int v, int regionWidth, int regionHeight, int textureWidth, int textureHeight) {
        int i, j;
        for (i = 0; i + regionWidth < width; i += regionWidth) {
            for (j = 0; j + regionHeight < height; j += regionHeight)
                context.drawTexture(texture, x + i, y + j, u, v, regionWidth, regionHeight, textureWidth, textureHeight);
            context.drawTexture(texture, x + i, y + j, u, v, regionWidth, height - j, textureWidth, textureHeight);
        }
        for (j = 0; j + regionHeight < height; j += regionHeight)
            context.drawTexture(texture, x + i, y + j, u, v, width - i, regionHeight, textureWidth, textureHeight);
        context.drawTexture(texture, x + i, y + j, u, v, width - i, height - j, textureWidth, textureHeight);
    }

    private static class TooltipPage {
        private int x;
        private int y;
        private int width;
        private int height;
        private final List<TooltipComponent> components;

        private TooltipPage() {
            this(0, 0, 0, 0, new ArrayList<>());
        }

        private TooltipPage(int x, int y, int width, int height, List<TooltipComponent> components) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.components = components;
        }
    }
}
