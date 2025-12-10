package com.iafenvoy.tooltipsreforged.config.mode;

@SuppressWarnings("unused")
public enum ContainerPreviewRenderMode {
    VANILLA(true, false),
    NONE(false, false),
    IMAGE(false, true);

    private final boolean renderText, renderImage;

    ContainerPreviewRenderMode(boolean renderText, boolean renderImage) {
        this.renderText = renderText;
        this.renderImage = renderImage;
    }

    public boolean shouldRenderText() {
        return this.renderText;
    }

    public boolean shouldRenderImage() {
        return this.renderImage;
    }
}