package com.iafenvoy.tooltipsreforged.config;

import com.iafenvoy.jupiter.interfaces.IConfigEnumEntry;
import com.iafenvoy.tooltipsreforged.TooltipReforgedClient;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public enum EnchantmentsRenderMode implements IConfigEnumEntry {
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

    @Override
    public Text getDisplayText() {
        return Text.translatable("config.%s.enchantments.render_mode.%s".formatted(TooltipReforgedClient.MOD_ID, this.name().toLowerCase(Locale.ROOT)));
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
        EnchantmentsRenderMode[] types = values();
        return types[(this.ordinal() + (b ? 1 : -1)) % types.length];
    }
}
