package com.iafenvoy.tooltipsreforged.config;

import com.iafenvoy.jupiter.config.container.AutoInitConfigContainer;
import com.iafenvoy.jupiter.config.entry.BooleanEntry;
import com.iafenvoy.jupiter.config.entry.DoubleEntry;
import com.iafenvoy.jupiter.config.entry.IntegerEntry;
import com.iafenvoy.jupiter.config.entry.SeparatorEntry;
import com.iafenvoy.jupiter.interfaces.IConfigEntry;
import com.iafenvoy.tooltipsreforged.TooltipReforgedClient;
import net.minecraft.util.Identifier;

import java.awt.*;

public class TooltipReforgedConfig extends AutoInitConfigContainer {
    public static final TooltipReforgedConfig INSTANCE = new TooltipReforgedConfig();

    public final CommonConfig common = new CommonConfig();

    public Color backgroundColor = new Color(this.common.backgroundColor.getValue(), true);

    public TooltipReforgedConfig() {
        super(Identifier.of(TooltipReforgedClient.MOD_ID, TooltipReforgedClient.MOD_ID), "screen.%s.title".formatted(TooltipReforgedClient.MOD_ID), "./config/tooltips_reforged.json");
    }

    @SuppressWarnings("unused")
    public static class CommonConfig extends AutoInitConfigCategoryBase {
        public IConfigEntry<Boolean> rarityTooltip = new BooleanEntry("config.tooltips_reforged.rarityTooltip", false).json("rarityTooltip");
        public IConfigEntry<Boolean> hungerTooltip = new BooleanEntry("config.tooltips_reforged.hungerTooltip", true).json("hungerTooltip");
        public IConfigEntry<Boolean> saturationTooltip = new BooleanEntry("config.tooltips_reforged.saturationTooltip", true).json("saturationTooltip");
        public IConfigEntry<Boolean> effectsTooltip = new BooleanEntry("config.tooltips_reforged.effectsTooltip", true).json("effectsTooltip");
        public IConfigEntry<Boolean> armorTooltip = new BooleanEntry("config.tooltips_reforged.armorTooltip", true).json("armorTooltip");
        public IConfigEntry<Boolean> bucketTooltip = new BooleanEntry("config.tooltips_reforged.bucketTooltip", true).json("bucketTooltip");
        public IConfigEntry<Boolean> spawnEggTooltip = new BooleanEntry("config.tooltips_reforged.spawnEggTooltip", true).json("spawnEggTooltip");
        public IConfigEntry<Boolean> enhancedDurabilityTooltip = new BooleanEntry("config.tooltips_reforged.enhancedDurabilityTooltip", true).json("enhancedDurabilityTooltip");
        public IConfigEntry<Boolean> durabilityBar = new BooleanEntry("config.tooltips_reforged.durabilityBar", true).json("durabilityBar");
        public IConfigEntry<Boolean> paintingTooltip = new BooleanEntry("config.tooltips_reforged.paintingTooltip", true).json("paintingTooltip");
        public IConfigEntry<Boolean> mapTooltip = new BooleanEntry("config.tooltips_reforged.mapTooltip", true).json("mapTooltip");
        public IConfigEntry<Boolean> itemGroupTooltip = new BooleanEntry("config.tooltips_reforged.itemGroupTooltip", true).json("itemGroupTooltip");
        public IConfigEntry<Boolean> debugInfoTooltip = new BooleanEntry("config.tooltips_reforged.debugInfoTooltip", true).json("debugInfoTooltip");
        public IConfigEntry<Boolean> enchantmentTooltip = new BooleanEntry("config.tooltips_reforged.enchantmentTooltip", true).json("enchantmentTooltip");
        public SeparatorEntry s1 = new SeparatorEntry();
        public IConfigEntry<Boolean> usePlayer = new BooleanEntry("config.tooltips_reforged.usePlayer", false).json("usePlayer");
        public IConfigEntry<Double> scaleFactor = new DoubleEntry("config.tooltips_reforged.scaleFactor", 1.0).json("scaleFactor");
        public IConfigEntry<Boolean> useNameColor = new BooleanEntry("config.tooltips_reforged.useNameColor", false).json("useNameColor");
        public IConfigEntry<Integer> backgroundColor = new IntegerEntry("config.tooltips_reforged.backgroundColor", 0xF0100010).json("backgroundColor");
        public IConfigEntry<Boolean> effectsIcon = new BooleanEntry("config.tooltips_reforged.effectsIcon", true).json("effectsIcon");

        public CommonConfig() {
            super("common", "screen.%s.common".formatted(TooltipReforgedClient.MOD_ID));
        }
    }
}
