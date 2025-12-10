package com.iafenvoy.tooltipsreforged.config.mode;

@SuppressWarnings("unused")
public enum EffectsRenderMode {
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
}
