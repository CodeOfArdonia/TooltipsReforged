package com.iafenvoy.tooltipsreforged;

import dev.architectury.injectables.annotations.ExpectPlatform;
import it.unimi.dsi.fastutil.longs.Long2ObjectArrayMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public final class Static {
    public static final int END_COLOR = 0x505000FF;
    public static final Long2ObjectMap<ItemStack> CACHE = new Long2ObjectArrayMap<>();

    @ExpectPlatform
    public static Map<String, String> getAllMods() {
        throw new AssertionError("This method should be replaced by Architectury.");
    }
}
