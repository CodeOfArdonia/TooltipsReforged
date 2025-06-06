package com.iafenvoy.tooltipsreforged.fabric;

import com.iafenvoy.jupiter.render.screen.ClientConfigScreen;
import com.iafenvoy.tooltipsreforged.config.TooltipReforgedConfig;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class ModMenu implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> new ClientConfigScreen(parent, TooltipReforgedConfig.INSTANCE);
    }
}
