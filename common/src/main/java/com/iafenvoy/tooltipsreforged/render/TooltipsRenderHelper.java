package com.iafenvoy.tooltipsreforged.render;

import com.iafenvoy.integration.entrypoint.EntryPointManager;
import com.iafenvoy.tooltipsreforged.BuiltinTooltips;
import com.iafenvoy.tooltipsreforged.TooltipReforgedClient;
import com.iafenvoy.tooltipsreforged.api.TooltipsReforgeEntrypoint;
import com.iafenvoy.tooltipsreforged.component.StandaloneComponent;
import com.iafenvoy.tooltipsreforged.config.TooltipReforgedConfig;
import com.iafenvoy.tooltipsreforged.util.ExtendedTextVisitor;
import com.iafenvoy.tooltipsreforged.util.TextUtil;
import com.iafenvoy.tooltipsreforged.util.TooltipScrollTracker;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.tooltip.OrderedTextTooltipComponent;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.client.gui.tooltip.TooltipPositioner;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import org.joml.Vector2ic;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

@Environment(EnvType.CLIENT)
public class TooltipsRenderHelper {
    private static final int EDGE_SPACING = 32;
    private static final int PAGE_SPACING = 12;

    public static void render(ItemStack stack, List<TooltipComponent> components, DrawContext context, TextRenderer textRenderer, int mouseX, int mouseY, TooltipPositioner positioner) {
        BuiltinTooltips.appendTooltip(stack, components);
        EntryPointManager.getEntryPoints(TooltipReforgedClient.MOD_ID, TooltipsReforgeEntrypoint.class).forEach(e -> e.appendTooltip(stack, components));
        components.removeIf(Objects::isNull);
        if (TooltipReforgedConfig.INSTANCE.misc.removeEmptyLines.getValue())
            components.removeIf(x -> x instanceof OrderedTextTooltipComponent ordered && ExtendedTextVisitor.getText(TextUtil.getTextFromComponent(ordered)).getString().isEmpty());
        TooltipsRenderHelper.drawTooltip(stack, context, textRenderer, components, mouseX + TooltipScrollTracker.getXOffset(), mouseY + TooltipScrollTracker.getYOffset(), positioner);
    }

    public static void drawTooltip(ItemStack stack, DrawContext context, TextRenderer textRenderer, List<TooltipComponent> components, int mouseX, int mouseY, TooltipPositioner positioner) {
        if (components.isEmpty()) return;
        List<TooltipPage> pageList = new LinkedList<>();
        List<StandaloneComponent> standaloneComponents = new LinkedList<>();
        int maxWidth = getMaxWidth(), maxHeight = getMaxHeight();
        int totalWidth = 0, pageHeight = -2;
        //Collect pages
        TooltipPage p = new TooltipPage();
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
                    pageList.add(p);
                    totalWidth += p.width;
                    p = new TooltipPage();
                    pageHeight = -2;
                }

                p.components.add(component);
                p.height = pageHeight += wrappedHeight;
                p.width = Math.max(p.width, wrappedWidth);
            } else {
                if (pageHeight + height > maxHeight) {
                    pageList.add(p);
                    totalWidth += p.width;
                    p = new TooltipPage();
                    pageHeight = -2;
                }

                p.components.add(component);
                p.height = pageHeight += height;
                p.width = Math.max(p.width, width);
            }
        }
        if (!p.components.isEmpty()) {
            pageList.add(p);
            totalWidth += p.width;
        }
        //Set actual position
        Vector2ic position = positioner.getPosition(context.getScaledWindowWidth(), context.getScaledWindowHeight(), mouseX, mouseY, totalWidth, pageList.get(0).height);
        int currentX = position.x();
        for (TooltipPage tooltipPage : pageList) {
            tooltipPage.x = currentX;
            tooltipPage.y = position.y() - (pageList.size() > 1 ? EDGE_SPACING : 6);
            currentX += tooltipPage.width + PAGE_SPACING;
        }
        //Render start
        context.getMatrices().push();
        context.getMatrices().translate(0, 0, 400);
        for (TooltipPage page : pageList) {
            ExtendedTooltipBackgroundRenderer.render(stack, context, page.x, page.y, page.width, page.height, 0);
            int currentY = page.y;
            for (TooltipComponent component : page.components) {
                try {
                    component.drawText(textRenderer, page.x, currentY, context.getMatrices().peek().getPositionMatrix(), context.getVertexConsumers());
                    component.drawItems(textRenderer, page.x, currentY, context);
                    currentY += component.getHeight();
                } catch (Exception e) {
                    TooltipReforgedClient.LOGGER.error("{}", TooltipReforgedClient.MOD_ID, e);
                }
            }
        }
        for (StandaloneComponent component : standaloneComponents)
            try {
                component.render(context, position.x(), position.y(), 0);
            } catch (Exception e) {
                TooltipReforgedClient.LOGGER.error("{}", TooltipReforgedClient.MOD_ID, e);
            }
        context.getMatrices().pop();
    }

    private static int getMaxHeight() {
        return MinecraftClient.getInstance().getWindow().getScaledHeight() - EDGE_SPACING * 2;
    }

    private static int getMaxWidth() {
        return MinecraftClient.getInstance().getWindow().getScaledWidth() / 2 - EDGE_SPACING;
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
            this(0, 0, 0, 0, new LinkedList<>());
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
