package com.iafenvoy.tooltipsreforged.forge;

import net.minecraftforge.fml.ModList;
import net.minecraftforge.forgespi.language.IModInfo;

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
}
