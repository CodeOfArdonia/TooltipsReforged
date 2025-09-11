package com.iafenvoy.tooltipsreforged.forge;

import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.client.item.TooltipData;
import net.minecraftforge.client.gui.ClientTooltipComponentManager;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.forgespi.language.IModInfo;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public final class StaticImpl {
    public static Map<String, String> getAllMods() {
        Map<String, String> mods = new HashMap<>();
        for (IModInfo modContainer : ModList.get().getMods()) {
            if (modContainer.getModId().equals("minecraft")) continue;
            mods.put(modContainer.getNamespace(), modContainer.getDisplayName());
        }
        return mods;
    }

    @Nullable
    public static TooltipComponent getExtraComponent(@NotNull TooltipData data) {
        return ClientTooltipComponentManager.createClientTooltipComponent(data);
    }
}
