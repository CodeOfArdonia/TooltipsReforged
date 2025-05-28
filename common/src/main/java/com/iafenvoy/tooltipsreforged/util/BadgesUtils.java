package com.iafenvoy.tooltipsreforged.util;

import com.iafenvoy.tooltipsreforged.Static;
import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.objects.Object2IntLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Objects;

public class BadgesUtils {
    private static final Map<String, String> MODS_MAP;
    private static final Object2IntMap<String> COLOR_MAP = new Object2IntLinkedOpenHashMap<>();

    static {
        MODS_MAP = Static.getAllMods();
        COLOR_MAP.put("building_blocks", 0xfff2c94c);
        COLOR_MAP.put("colored_blocks", 0xff42a5f5);
        COLOR_MAP.put("natural_blocks", 0xff66bb6a);
        COLOR_MAP.put("functional_blocks", 0xff2a9d8f);
        COLOR_MAP.put("redstone_blocks", 0xffff6b6b);
        COLOR_MAP.put("tools_and_utilities", 0xff9b59b6);
        COLOR_MAP.put("combat", 0xfff94144);
        COLOR_MAP.put("food_and_drinks", 0xff61b748);
        COLOR_MAP.put("ingredients", 0xffff6347);
        COLOR_MAP.put("spawn_eggs", 0xffa29bfe);
        COLOR_MAP.put("op_blocks", 0xff9c89b8);
    }

    public static @NotNull Pair<Text, Integer> getBadgeText(ItemStack stack) {
        for (ItemGroup group : ItemGroups.getGroupsToDisplay())
            if (group.getType() == ItemGroup.Type.CATEGORY && group.contains(stack))
                return new Pair<>(group.getDisplayName(), COLOR_MAP.getOrDefault(Objects.requireNonNullElse(Registries.ITEM_GROUP.getId(group), Identifier.of("", "")).getPath(), 0xff000000));
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
