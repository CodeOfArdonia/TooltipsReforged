package com.iafenvoy.tooltipsreforged.config;

import com.iafenvoy.jupiter.interfaces.IConfigEnumEntry;
import com.iafenvoy.tooltipsreforged.TooltipReforgedClient;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public enum ItemDisplayMode implements IConfigEnumEntry {
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

    @Override
    public Text getDisplayText() {
        return Text.translatable("config.%s.header.item_display.%s".formatted(TooltipReforgedClient.MOD_ID, this.name().toLowerCase(Locale.ROOT)));
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
        ItemDisplayMode[] types = values();
        return types[(this.ordinal() + (b ? 1 : -1)) % types.length];
    }
}
