package com.iafenvoy.tooltipsreforged.config.mode;

@SuppressWarnings("unused")
public enum ItemDisplayMode {
    NONE(false, false),
    HEADER(true, false),
    ZOOM(false, true),
    BOTH(true, true);

    private final boolean renderHeader, renderZoom;

    ItemDisplayMode(boolean renderHeader, boolean renderZoom) {
        this.renderHeader = renderHeader;
        this.renderZoom = renderZoom;
    }

    public boolean shouldRenderHeader() {
        return this.renderHeader;
    }

    public boolean shouldRenderZoom() {
        return this.renderZoom;
    }
}
