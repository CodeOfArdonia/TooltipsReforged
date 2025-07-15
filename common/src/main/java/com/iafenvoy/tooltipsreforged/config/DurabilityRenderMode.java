package com.iafenvoy.tooltipsreforged.config;

import com.iafenvoy.jupiter.interfaces.IConfigEnumEntry;
import com.iafenvoy.tooltipsreforged.TooltipReforgedClient;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

//Vanilla: 64/64
public enum DurabilityRenderMode implements IConfigEnumEntry {
    VANILLA(false, false, false),
    VANILLA_COLORED(true, false, false),
    VANILLA_BACKGROUND(false, true, false),
    PERCENTAGE_COLORED(true, false, true),
    PERCENTAGE_BACKGROUND(false, true, true);

    private final boolean colorText, renderBackground, percentage;

    DurabilityRenderMode(boolean colorText, boolean renderBackground, boolean percentage) {
        this.colorText = colorText;
        this.renderBackground = renderBackground;
        this.percentage = percentage;
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


    @Override
    public Text getDisplayText() {
        return Text.translatable("config.%s.durability.render_mode.%s".formatted(TooltipReforgedClient.MOD_ID, this.name().toLowerCase(Locale.ROOT)));
    }

    @Override
    public String getName() {
        return this.name();
    }

    @Override
    public @NotNull IConfigEnumEntry getByName(String s) {
        return valueOf(s);
    }

    @Override
    public IConfigEnumEntry cycle(boolean b) {
        DurabilityRenderMode[] types = values();
        return types[(this.ordinal() + (b ? 1 : -1)) % types.length];
    }
}
