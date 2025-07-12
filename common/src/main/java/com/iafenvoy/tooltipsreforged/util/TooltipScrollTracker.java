package com.iafenvoy.tooltipsreforged.util;

import net.minecraft.util.math.MathHelper;

public class TooltipScrollTracker {
    private static double currentXOffset, currentYOffset, trueXOffset, trueYOffset;
    public static final int SCROLL_SIZE = 10;
    public static final double SMOOTHNESS_MODIFIER = 0.25;

    public static void tick() {
        currentXOffset += (trueXOffset - currentXOffset) * SMOOTHNESS_MODIFIER;
        currentYOffset += (trueYOffset - currentYOffset) * SMOOTHNESS_MODIFIER;
    }

    public static int getXOffset() {
        return MathHelper.floor(currentXOffset);
    }

    public static int getYOffset() {
        return MathHelper.floor(currentYOffset);
    }

    public static void scrollUp() {
        trueYOffset -= SCROLL_SIZE;
    }

    public static void scrollDown() {
        trueYOffset += SCROLL_SIZE;
    }

    public static void scrollLeft() {
        trueXOffset -= SCROLL_SIZE;
    }

    public static void scrollRight() {
        trueXOffset += SCROLL_SIZE;
    }

    public static void resetScroll() {
        currentXOffset = 0;
        currentYOffset = 0;
        trueXOffset = 0;
        trueYOffset = 0;
    }
}
