package com.iafenvoy.tooltipsreforged;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.client.item.TooltipData;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public final class Static {
    public static final int END_COLOR = 0x505000FF;
    public static final ThreadLocal<ItemStack> CACHE = new ThreadLocal<>();

    @ExpectPlatform
    public static Map<String, String> getAllMods() {
        throw new AssertionError("This method should be replaced by Architectury.");
    }

    @SuppressWarnings("ConstantValue")
    @Nullable
    public static TooltipComponent getExtraComponent(ItemStack stack) {
        return stack.getTooltipData().map(Static::getExtraComponent).orElse(null);
    }

    @ExpectPlatform
    @Nullable
    public static TooltipComponent getExtraComponent(@NotNull TooltipData data) {
        throw new AssertionError("This method should be replaced by Architectury.");
    }
}
