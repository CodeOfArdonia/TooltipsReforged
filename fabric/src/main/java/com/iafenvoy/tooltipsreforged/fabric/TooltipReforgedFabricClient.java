package com.iafenvoy.tooltipsreforged.fabric;

import com.iafenvoy.tooltipsreforged.TooltipReforgedClient;
import net.fabricmc.api.ClientModInitializer;

public final class TooltipReforgedFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        TooltipReforgedClient.init();
        FabricEntryPointLoader.init();
    }
}
