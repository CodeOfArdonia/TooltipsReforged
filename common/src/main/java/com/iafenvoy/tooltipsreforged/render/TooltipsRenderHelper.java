package com.iafenvoy.tooltipsreforged.render;

import com.iafenvoy.integration.entrypoint.EntryPointManager;
import com.iafenvoy.tooltipsreforged.BuiltinTooltips;
import com.iafenvoy.tooltipsreforged.TooltipReforgedClient;
import com.iafenvoy.tooltipsreforged.api.TooltipsReforgeEntrypoint;
import com.iafenvoy.tooltipsreforged.component.StandaloneComponent;
import com.iafenvoy.tooltipsreforged.config.TooltipReforgedConfig;
import com.iafenvoy.tooltipsreforged.util.ExtendedTextVisitor;
import com.iafenvoy.tooltipsreforged.util.TextUtil;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.tooltip.OrderedTextTooltipComponent;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.client.gui.tooltip.TooltipPositioner;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.DynamicRegistryManager;
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
        ClientWorld world = MinecraftClient.getInstance().world;
        if (world == null) return;
        DynamicRegistryManager registries = world.getRegistryManager();
        BuiltinTooltips.appendTooltip(stack, components, registries);
        EntryPointManager.getEntryPoints(TooltipReforgedClient.MOD_ID, TooltipsReforgeEntrypoint.class).forEach(e -> e.appendTooltip(stack, components, registries));
        components.removeIf(Objects::isNull);
        if (TooltipReforgedConfig.INSTANCE.misc.removeEmptyLines.getValue())
            components.removeIf(x -> x instanceof OrderedTextTooltipComponent ordered && ExtendedTextVisitor.getText(TextUtil.getTextFromComponent(ordered)).getString().isEmpty());
        ResolveResult result = resolveTooltips(textRenderer, components);
        Vector2ic position = resolvePosition(result, context, mouseX, mouseY, positioner);
        TooltipsRenderHelper.drawWithResult(result, stack, context, textRenderer, position.x(), position.y());
    }

    public static ResolveResult resolveTooltips(TextRenderer textRenderer, List<TooltipComponent> components) {
        if (components.isEmpty()) return new ResolveResult(List.of(), List.of(), 0);
        List<Page> pageList = new LinkedList<>();
        List<StandaloneComponent> standaloneComponents = new LinkedList<>();
        int maxWidth = getMaxWidth(), maxHeight = getMaxHeight();
        int totalWidth = 0, pageHeight = -2;
        Page p = new Page();
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
                    p = new Page();
                    pageHeight = -2;
                }

                p.components.add(component);
                p.height = pageHeight += wrappedHeight;
                p.width = Math.max(p.width, wrappedWidth);
            } else {
                if (pageHeight + height > maxHeight) {
                    pageList.add(p);
                    totalWidth += p.width;
                    p = new Page();
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
        return new ResolveResult(pageList, standaloneComponents, totalWidth);
    }

    public static Vector2ic resolvePosition(ResolveResult result, DrawContext context, int mouseX, int mouseY, TooltipPositioner positioner) {
        return positioner.getPosition(context.getScaledWindowWidth(), context.getScaledWindowHeight(), mouseX, mouseY, result.totalWidth, result.pages.getFirst().height);
    }

    public static void drawWithResult(ResolveResult result, ItemStack stack, DrawContext context, TextRenderer textRenderer, int x, int y) {
        if (result.pages.isEmpty()) return;
        //Set actual position
        int currentX = x;
        for (Page page : result.pages) {
            page.x = currentX;
            page.y = y - (result.pages.size() > 1 ? EDGE_SPACING : 6);
            currentX += page.width + PAGE_SPACING;
        }
        //Render start
        context.getMatrices().push();
        context.getMatrices().translate(0, 0, 400);
        for (Page page : result.pages) {
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
        for (StandaloneComponent component : result.standalone)
            try {
                component.render(context, textRenderer, x, y, 0);
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

    public record ResolveResult(List<Page> pages, List<StandaloneComponent> standalone, int totalWidth) {
    }

    public static class Page {
        private int x;
        private int y;
        private int width;
        private int height;
        private final List<TooltipComponent> components;

        private Page() {
            this(0, 0, 0, 0, new LinkedList<>());
        }

        private Page(int x, int y, int width, int height, List<TooltipComponent> components) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.components = components;
        }

        public int getHeight() {
            return this.height;
        }
    }
}
