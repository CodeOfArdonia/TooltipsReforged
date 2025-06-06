package com.iafenvoy.tooltipsreforged.fabric;

import com.iafenvoy.tooltipsreforged.TooltipReforgedClient;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public final class TooltipReforgedFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        TooltipReforgedClient.init();
        FabricEntryPointLoader.init();
    }
}
