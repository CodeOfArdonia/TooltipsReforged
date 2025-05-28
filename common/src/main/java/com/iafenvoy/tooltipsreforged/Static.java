package com.iafenvoy.tooltipsreforged;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public final class Static {
    public static final int END_COLOR = 0x505000FF;
    @Nullable
    public static ItemStack CACHE = null;

    @ExpectPlatform
    public static Map<String,String> getAllMods(){
        throw new AssertionError("This method should be replaced by Architectury.");
    }
}
