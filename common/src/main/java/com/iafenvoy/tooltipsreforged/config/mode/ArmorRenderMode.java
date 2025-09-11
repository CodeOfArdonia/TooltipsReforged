package com.iafenvoy.tooltipsreforged.config.mode;

import com.iafenvoy.jupiter.interfaces.IConfigEnumEntry;
import com.iafenvoy.tooltipsreforged.TooltipReforgedClient;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public enum ArmorRenderMode implements IConfigEnumEntry {
    NONE, ARMOR_STAND, PLAYER;

    @Override
    public Text getDisplayText() {
        return Text.translatable("config.%s.armor.render_mode.%s".formatted(TooltipReforgedClient.MOD_ID, this.name().toLowerCase(Locale.ROOT)));
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
        ArmorRenderMode[] types = values();
        return types[(this.ordinal() + (b ? 1 : -1)) % types.length];
    }
}
