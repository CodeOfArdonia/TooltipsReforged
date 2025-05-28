package com.iafenvoy.tooltipsreforged;

import com.iafenvoy.jupiter.ConfigManager;
import com.iafenvoy.tooltipsreforged.config.TooltipReforgedConfig;
import com.iafenvoy.tooltipsreforged.event.TooltipComponentEvent;
import com.mojang.logging.LogUtils;
import org.slf4j.Logger;

public final class TooltipReforgedClient {
    public static final String MOD_ID = "tooltips_reforged";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static void init() {
        ConfigManager.getInstance().registerConfigHandler(TooltipReforgedConfig.INSTANCE);
        TooltipComponentEvent.EVENT.register(TooltipManager::appendTooltip);
    }
}
