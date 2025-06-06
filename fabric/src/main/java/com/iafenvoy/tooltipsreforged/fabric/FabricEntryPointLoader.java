package com.iafenvoy.tooltipsreforged.fabric;

import com.iafenvoy.tooltipsreforged.EntryPointLoader;
import com.iafenvoy.tooltipsreforged.TooltipReforgedClient;
import com.iafenvoy.tooltipsreforged.api.TooltipsReforgeEntrypoint;
import net.fabricmc.loader.api.FabricLoader;

import java.util.List;

public class FabricEntryPointLoader extends EntryPointLoader {
    public static void init() {
        INSTANCE = new FabricEntryPointLoader();
    }

    @Override
    protected List<TooltipsReforgeEntrypoint> loadEntries() {
        return FabricLoader.getInstance().getEntrypoints(TooltipReforgedClient.MOD_ID, TooltipsReforgeEntrypoint.class);
    }
}
