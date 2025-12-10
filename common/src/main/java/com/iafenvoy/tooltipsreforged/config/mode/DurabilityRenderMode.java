package com.iafenvoy.tooltipsreforged.config.mode;

//Vanilla: 64/64
@SuppressWarnings("unused")
public enum DurabilityRenderMode {
    NONE(false, false, false, false),
    VANILLA(true, false, false, false),
    VANILLA_COLORED(true, true, false, false),
    VANILLA_BACKGROUND(true, false, true, false),
    PERCENTAGE_COLORED(true, true, false, true),
    PERCENTAGE_BACKGROUND(true, false, true, true);

    private final boolean enabled, colorText, renderBackground, percentage;

    DurabilityRenderMode(boolean enabled, boolean colorText, boolean renderBackground, boolean percentage) {
        this.enabled = enabled;
        this.colorText = colorText;
        this.renderBackground = renderBackground;
        this.percentage = percentage;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public boolean shouldColorText() {
        return this.colorText;
    }

    public boolean shouldInPercentage() {
        return this.percentage;
    }

    public boolean shouldRenderBackground() {
        return this.renderBackground;
    }
}
