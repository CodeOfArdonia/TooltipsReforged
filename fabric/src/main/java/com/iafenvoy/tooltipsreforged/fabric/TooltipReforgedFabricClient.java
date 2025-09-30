package com.iafenvoy.tooltipsreforged.fabric;

import com.iafenvoy.tooltipsreforged.TooltipReforgedClient;
import com.iafenvoy.tooltipsreforged.util.TooltipKeyManager;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.impl.client.keybinding.KeyBindingRegistryImpl;

@Environment(EnvType.CLIENT)
public final class TooltipReforgedFabricClient implements ClientModInitializer {
    @SuppressWarnings("UnstableApiUsage")
    @Override
    public void onInitializeClient() {
        TooltipReforgedClient.init();
        KeyBindingRegistryImpl.registerKeyBinding(TooltipKeyManager.SHOW_ITEM_TAG);
        KeyBindingRegistryImpl.registerKeyBinding(TooltipKeyManager.SHOW_NBT_SPAWN_EGG);
        KeyBindingRegistryImpl.registerKeyBinding(TooltipKeyManager.SHOW_SPECIFIC_INFO);
    }
}
