package com.iafenvoy.tooltipsreforged.config;

import com.iafenvoy.jupiter.interfaces.IConfigEnumEntry;
import com.iafenvoy.tooltipsreforged.TooltipReforgedClient;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public enum ContainerPreviewRenderMode implements IConfigEnumEntry {
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

    @Override
    public Text getDisplayText() {
        return Text.translatable("config.%s.container.render_mode.%s".formatted(TooltipReforgedClient.MOD_ID, this.name().toLowerCase(Locale.ROOT)));
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
        ContainerPreviewRenderMode[] types = values();
        return types[(this.ordinal() + (b ? 1 : -1)) % types.length];
    }
}