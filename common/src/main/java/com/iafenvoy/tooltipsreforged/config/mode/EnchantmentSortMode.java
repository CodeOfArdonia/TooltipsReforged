package com.iafenvoy.tooltipsreforged.config.mode;

import com.iafenvoy.tooltipsreforged.component.EnchantmentsComponent;

import java.util.Comparator;

@SuppressWarnings("unused")
public enum EnchantmentSortMode {
    DEFAULT(Comparator.comparing(e -> 0)),
    LEVEL(Comparator.comparingInt(EnchantmentsComponent.EnchantmentInfo::level).reversed()),
    NAME(Comparator.comparing(EnchantmentsComponent.EnchantmentInfo::id)),
    TYPE(Comparator.comparingInt(e -> e.enchantment().isCursed() ? 1 : 0));
    private final Comparator<EnchantmentsComponent.EnchantmentInfo> comparator;

    EnchantmentSortMode(Comparator<EnchantmentsComponent.EnchantmentInfo> comparator) {
        this.comparator = comparator;
    }

    public Comparator<EnchantmentsComponent.EnchantmentInfo> getComparator() {
        return this.comparator;
    }
}

