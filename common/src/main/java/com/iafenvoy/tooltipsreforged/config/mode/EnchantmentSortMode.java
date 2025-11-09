package com.iafenvoy.tooltipsreforged.config.mode;

import com.iafenvoy.jupiter.interfaces.IConfigEnumEntry;
import com.iafenvoy.tooltipsreforged.TooltipReforgedClient;
import com.iafenvoy.tooltipsreforged.component.EnchantmentsComponent;
import net.minecraft.registry.tag.EnchantmentTags;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.Locale;

public enum EnchantmentSortMode implements IConfigEnumEntry {
    DEFAULT(Comparator.comparing(e -> 0)),
    LEVEL(Comparator.comparingInt(EnchantmentsComponent.EnchantmentInfo::level).reversed()),
    NAME(Comparator.comparing(EnchantmentsComponent.EnchantmentInfo::id)),
    TYPE(Comparator.comparingInt(e -> e.enchantment().isIn(EnchantmentTags.CURSE) ? 1 : 0));
    private final Comparator<EnchantmentsComponent.EnchantmentInfo> comparator;

    EnchantmentSortMode(Comparator<EnchantmentsComponent.EnchantmentInfo> comparator) {
        this.comparator = comparator;
    }

    public Comparator<EnchantmentsComponent.EnchantmentInfo> getComparator() {
        return this.comparator;
    }

    @Override
    public Text getDisplayText() {
        return Text.translatable("config.%s.enchantment.sort.%s".formatted(TooltipReforgedClient.MOD_ID, this.name().toLowerCase(Locale.ROOT)));
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
        EnchantmentSortMode[] types = values();
        return types[(this.ordinal() + (b ? 1 : -1)) % types.length];
    }
}

