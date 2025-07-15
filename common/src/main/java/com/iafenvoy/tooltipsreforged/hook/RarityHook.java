package com.iafenvoy.tooltipsreforged.hook;

import net.minecraft.text.MutableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Rarity;
import org.jetbrains.annotations.ApiStatus;

import java.util.LinkedList;
import java.util.List;
import java.util.function.BiFunction;

//Forge extended Rarity class, so we need this.
public final class RarityHook {
    private static final List<BiFunction<MutableText, Rarity, MutableText>> HOOKS = new LinkedList<>();

    public static void register(BiFunction<MutableText, Rarity, MutableText> hook) {
        HOOKS.add(hook);
    }

    @ApiStatus.Internal
    public static MutableText applyColor(MutableText text, Rarity rarity) {
        return HOOKS.stream().collect(() -> text, (p, c) -> c.apply(p, rarity), MutableText::append);
    }

    static {
        register((text, rarity) -> text.formatted(rarity.formatting == Formatting.BLACK ? Formatting.WHITE : rarity.formatting));
    }
}
