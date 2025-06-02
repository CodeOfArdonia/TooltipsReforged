package com.iafenvoy.tooltipsreforged.util;

import com.iafenvoy.tooltipsreforged.Static;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public final class BadgesUtils {
    private static final Map<String, String> MODS_MAP = Static.getAllMods();

    public static @NotNull Pair<Text, Integer> getBadgeText(ItemStack stack) {
        String namespace = Registries.ITEM.getId(stack.getItem()).getNamespace();
        return new Pair<>(Text.literal(MODS_MAP.getOrDefault(namespace, "Minecraft")), getColorFromModName(namespace));
    }

    public static int darkenColor(int color, float factor) {
        int alpha = (color >> 24) & 0xFF;
        int red = (color >> 16) & 0xFF;
        int green = (color >> 8) & 0xFF;
        int blue = color & 0xFF;

        red = Math.max(0, (int) (red * factor));
        green = Math.max(0, (int) (green * factor));
        blue = Math.max(0, (int) (blue * factor));

        return (alpha << 24) | (red << 16) | (green << 8) | blue;
    }

    public static int getColorFromModName(String modName) {
        int hash = modName.hashCode();

        int r = (hash >> 16) & 0xFF;
        int g = (hash >> 8) & 0xFF;
        int b = hash & 0xFF;
        int a = 0xFF;

        return (a << 24) | (r << 16) | (g << 8) | b;
    }

    public static void drawFrame(DrawContext context, int x, int y, int width, int height, int z, int color) {
        renderVerticalLine(context, x, y, height - 2, z, color);
        renderVerticalLine(context, x + width - 1, y, height - 2, z, color);
        renderHorizontalLine(context, x + 1, y - 1, width - 2, z, color);
        renderHorizontalLine(context, x + 1, y - 1 + height - 1, width - 2, z, color);
    }

    private static void renderVerticalLine(DrawContext context, int x, int y, int height, int z, int color) {
        context.fill(x, y, x + 1, y + height, z, color);
    }

    private static void renderHorizontalLine(DrawContext context, int x, int y, int width, int z, int color) {
        context.fill(x, y, x + width, y + 1, z, color);
    }
}
