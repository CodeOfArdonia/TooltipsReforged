package com.iafenvoy.tooltipsreforged.config;

import com.iafenvoy.jupiter.interfaces.IConfigEnumEntry;
import com.iafenvoy.tooltipsreforged.TooltipReforgedClient;
import com.iafenvoy.tooltipsreforged.component.FoodEffectComponent;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public enum EffectsRenderMode implements IConfigEnumEntry {
    NONE(false, false),
    WITH_ICON(true, true),
    WITHOUT_ICON(true, false);

    private final boolean render, renderIcon;

    EffectsRenderMode(boolean render, boolean renderIcon) {
        this.render = render;
        this.renderIcon = renderIcon;
    }

    public boolean shouldRender() {
        return this.render;
    }

    public boolean shouldRenderIcon() {
        return this.renderIcon;
    }

    @Override
    public String getName() {
        return "config.%s.effects.render_mode.%s".formatted(TooltipReforgedClient.MOD_ID, this.name().toLowerCase(Locale.ROOT));
    }

    @Override
    public @NotNull IConfigEnumEntry getByName(String s) {
        return valueOf(s);
    }

    @Override
    public IConfigEnumEntry cycle(boolean b) {
        EffectsRenderMode[] types = values();
        return types[(this.ordinal() + (b ? 1 : -1)) % types.length];
    }
}
