package com.iafenvoy.tooltipsreforged.config;

import com.iafenvoy.jupiter.config.container.AutoInitConfigContainer;
import com.iafenvoy.jupiter.config.entry.BooleanEntry;
import com.iafenvoy.jupiter.config.entry.DoubleEntry;
import com.iafenvoy.jupiter.config.entry.IntegerEntry;
import com.iafenvoy.jupiter.config.entry.SeparatorEntry;
import com.iafenvoy.jupiter.interfaces.IConfigEntry;
import com.iafenvoy.tooltipsreforged.TooltipReforgedClient;
import net.minecraft.util.Identifier;

public class TooltipReforgedConfig extends AutoInitConfigContainer {
    public static final TooltipReforgedConfig INSTANCE = new TooltipReforgedConfig();

    public final CommonConfig common = new CommonConfig();

    public TooltipReforgedConfig() {
        super(Identifier.of(TooltipReforgedClient.MOD_ID, TooltipReforgedClient.MOD_ID), "screen.%s.title".formatted(TooltipReforgedClient.MOD_ID), "./config/tooltips_reforged.json");
    }

    @SuppressWarnings("unused")
    public static class CommonConfig extends AutoInitConfigCategoryBase {
        public final IConfigEntry<Boolean> rarityTooltip = new BooleanEntry("config.tooltips_reforged.rarityTooltip", false).json("rarityTooltip");
        public final IConfigEntry<Boolean> hungerTooltip = new BooleanEntry("config.tooltips_reforged.hungerTooltip", true).json("hungerTooltip");
        public final IConfigEntry<Boolean> saturationTooltip = new BooleanEntry("config.tooltips_reforged.saturationTooltip", true).json("saturationTooltip");
        public final IConfigEntry<Boolean> effectsTooltip = new BooleanEntry("config.tooltips_reforged.effectsTooltip", true).json("effectsTooltip");
        public final IConfigEntry<Boolean> armorTooltip = new BooleanEntry("config.tooltips_reforged.armorTooltip", true).json("armorTooltip");
        public final IConfigEntry<Boolean> bucketTooltip = new BooleanEntry("config.tooltips_reforged.bucketTooltip", true).json("bucketTooltip");
        public final IConfigEntry<Boolean> spawnEggTooltip = new BooleanEntry("config.tooltips_reforged.spawnEggTooltip", true).json("spawnEggTooltip");
        public final IConfigEntry<Boolean> durabilityTooltip = new BooleanEntry("config.tooltips_reforged.durabilityTooltip", true).json("durabilityTooltip");
        public final IConfigEntry<Boolean> containerTooltip = new BooleanEntry("config.tooltips_reforged.containerTooltip", true).json("containerTooltip");
        public final IConfigEntry<Boolean> paintingTooltip = new BooleanEntry("config.tooltips_reforged.paintingTooltip", true).json("paintingTooltip");
        public final IConfigEntry<Boolean> bannerTooltip = new BooleanEntry("config.tooltips_reforged.bannerTooltip", true).json("bannerTooltip");
        public final IConfigEntry<Boolean> mapTooltip = new BooleanEntry("config.tooltips_reforged.mapTooltip", true).json("mapTooltip");
        public final IConfigEntry<Boolean> itemGroupTooltip = new BooleanEntry("config.tooltips_reforged.itemGroupTooltip", true).json("itemGroupTooltip");
        public final IConfigEntry<Boolean> debugInfoTooltip = new BooleanEntry("config.tooltips_reforged.debugInfoTooltip", true).json("debugInfoTooltip");
        public final IConfigEntry<Boolean> enchantmentTooltip = new BooleanEntry("config.tooltips_reforged.enchantmentTooltip", true).json("enchantmentTooltip");
        public final SeparatorEntry s1 = new SeparatorEntry();
        public final IConfigEntry<Boolean> percentageDurability = new BooleanEntry("config.tooltips_reforged.percentageDurability", false).json("percentageDurability");
        public final IConfigEntry<Boolean> durabilityBackground = new BooleanEntry("config.tooltips_reforged.durabilityBackground", true).json("durabilityBackground");
        public final IConfigEntry<Boolean> usePlayer = new BooleanEntry("config.tooltips_reforged.usePlayer", false).json("usePlayer");
        public final IConfigEntry<Double> scaleFactor = new DoubleEntry("config.tooltips_reforged.scaleFactor", 1.0).json("scaleFactor");
        public final IConfigEntry<Boolean> useNameColor = new BooleanEntry("config.tooltips_reforged.useNameColor", false).json("useNameColor");
        public final IConfigEntry<Integer> backgroundColor = new IntegerEntry("config.tooltips_reforged.backgroundColor", 0xF0100010).json("backgroundColor");
        public final IConfigEntry<Boolean> effectsIcon = new BooleanEntry("config.tooltips_reforged.effectsIcon", true).json("effectsIcon");
        public final IConfigEntry<Boolean> mouseScrollTooltip = new BooleanEntry("config.tooltips_reforged.mouseScrollTooltip", true).json("mouseScrollTooltip");
        public final IConfigEntry<Integer> itemBorderColor = new IntegerEntry("config.tooltips_reforged.itemBorderColor", 0xFFAAAAAA).json("itemBorderColor");

        public CommonConfig() {
            super("common", "screen.%s.common".formatted(TooltipReforgedClient.MOD_ID));
        }
    }
}
