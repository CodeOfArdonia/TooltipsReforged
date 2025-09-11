package com.iafenvoy.tooltipsreforged.fabric;

import net.fabricmc.fabric.api.client.rendering.v1.TooltipComponentCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.client.item.TooltipData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public final class StaticImpl {
    public static Map<String, String> getAllMods() {
        Map<String, String> mods = new HashMap<>();
        for (ModContainer modContainer : FabricLoader.getInstance().getAllMods()) {
            if (modContainer.getMetadata().getId().equals("minecraft")) continue;
            mods.put(modContainer.getMetadata().getId(), modContainer.getMetadata().getName());
        }
        return mods;
    }

    @Nullable
    public static TooltipComponent getExtraComponent(@NotNull TooltipData data) {
        return TooltipComponentCallback.EVENT.invoker().getComponent(data);
    }
}
