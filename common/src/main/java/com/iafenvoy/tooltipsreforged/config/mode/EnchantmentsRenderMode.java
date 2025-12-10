package com.iafenvoy.tooltipsreforged.config.mode;

@SuppressWarnings("unused")
public enum EnchantmentsRenderMode {
    NONE(false, false),
    SHIFT_DETAIL(true, false),
    ALL(true, true);

    private final boolean render, alwaysDescription;

    EnchantmentsRenderMode(boolean render, boolean alwaysDescription) {
        this.render = render;
        this.alwaysDescription = alwaysDescription;
    }

    public boolean shouldRender() {
        return this.render;
    }

    public boolean shouldAlwaysDescription() {
        return this.alwaysDescription;
    }
}
