package com.iafenvoy.tooltipsreforged.config;

import com.iafenvoy.jupiter.interfaces.IConfigEnumEntry;
import com.iafenvoy.tooltipsreforged.TooltipReforgedClient;
import com.iafenvoy.tooltipsreforged.component.ModelViewerComponent;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public enum ArmorRenderMode implements IConfigEnumEntry {
    NONE, ARMOR_STAND, PLAYER;

    @Override
    public String getName() {
        return "config.%s.armor.render_mode.%s".formatted(TooltipReforgedClient.MOD_ID, this.name().toLowerCase(Locale.ROOT));
    }

    @Override
    public @NotNull IConfigEnumEntry getByName(String s) {
        return valueOf(s);
    }

    @Override
    public IConfigEnumEntry cycle(boolean b) {
        ArmorRenderMode[] types = values();
        return types[(this.ordinal() + (b ? 1 : -1)) % types.length];
    }
}
