package com.iafenvoy.tooltipsreforged.config;

import com.iafenvoy.jupiter.config.container.AutoInitConfigContainer;
import com.iafenvoy.jupiter.config.entry.BooleanEntry;
import com.iafenvoy.jupiter.config.entry.DoubleEntry;
import com.iafenvoy.jupiter.config.entry.EnumEntry;
import com.iafenvoy.jupiter.config.entry.IntegerEntry;
import com.iafenvoy.jupiter.interfaces.IConfigEntry;
import com.iafenvoy.jupiter.interfaces.IConfigEnumEntry;
import com.iafenvoy.tooltipsreforged.TooltipReforgedClient;
import net.minecraft.util.Identifier;

public class TooltipReforgedConfig extends AutoInitConfigContainer {
    public static final TooltipReforgedConfig INSTANCE = new TooltipReforgedConfig();

    public final TooltipConfig tooltip = new TooltipConfig();
    public final MiscConfig misc = new MiscConfig();

    public TooltipReforgedConfig() {
        super(Identifier.of(TooltipReforgedClient.MOD_ID, TooltipReforgedClient.MOD_ID), "screen.%s.title".formatted(TooltipReforgedClient.MOD_ID), "./config/tooltips_reforged.json");
    }

    public static class TooltipConfig extends AutoInitConfigCategoryBase {
        public final IConfigEntry<Boolean> rarityTooltip = new BooleanEntry("config.tooltips_reforged.rarityTooltip", false).json("rarityTooltip");
        public final IConfigEntry<Boolean> hungerTooltip = new BooleanEntry("config.tooltips_reforged.hungerTooltip", true).json("hungerTooltip");
        public final IConfigEntry<Boolean> saturationTooltip = new BooleanEntry("config.tooltips_reforged.saturationTooltip", true).json("saturationTooltip");
        public final IConfigEntry<IConfigEnumEntry> effectsTooltip = new EnumEntry("config.tooltips_reforged.effectsTooltip", EffectsRenderMode.WITH_ICON).json("effectsTooltip");
        public final IConfigEntry<IConfigEnumEntry> armorTooltip = new EnumEntry("config.tooltips_reforged.armorTooltip", ArmorRenderMode.PLAYER).json("armorTooltip");
        public final IConfigEntry<Boolean> bucketTooltip = new BooleanEntry("config.tooltips_reforged.bucketTooltip", true).json("bucketTooltip");
        public final IConfigEntry<Boolean> spawnEggTooltip = new BooleanEntry("config.tooltips_reforged.spawnEggTooltip", true).json("spawnEggTooltip");
        public final IConfigEntry<IConfigEnumEntry> durabilityTooltip = new EnumEntry("config.tooltips_reforged.durabilityTooltip", DurabilityRenderMode.VANILLA_BACKGROUND).json("durabilityTooltip");
        public final IConfigEntry<IConfigEnumEntry> containerTooltip = new EnumEntry("config.tooltips_reforged.containerTooltip", ContainerPreviewRenderMode.IMAGE).json("containerTooltip");
        public final IConfigEntry<Boolean> paintingTooltip = new BooleanEntry("config.tooltips_reforged.paintingTooltip", true).json("paintingTooltip");
        public final IConfigEntry<Boolean> bannerTooltip = new BooleanEntry("config.tooltips_reforged.bannerTooltip", true).json("bannerTooltip");
        public final IConfigEntry<Boolean> mapTooltip = new BooleanEntry("config.tooltips_reforged.mapTooltip", true).json("mapTooltip");
        public final IConfigEntry<Boolean> itemGroupTooltip = new BooleanEntry("config.tooltips_reforged.itemGroupTooltip", true).json("itemGroupTooltip");
        public final IConfigEntry<Boolean> debugInfoTooltip = new BooleanEntry("config.tooltips_reforged.debugInfoTooltip", true).json("debugInfoTooltip");
        public final IConfigEntry<IConfigEnumEntry> enchantmentTooltip = new EnumEntry("config.tooltips_reforged.enchantmentTooltip", EnchantmentsRenderMode.SHIFT_DETAIL).json("enchantmentTooltip");

        public TooltipConfig() {
            super("common", "screen.%s.tooltip".formatted(TooltipReforgedClient.MOD_ID));
        }
    }

    public static class MiscConfig extends AutoInitConfigCategoryBase {
        public final IConfigEntry<Double> scaleFactor = new DoubleEntry("config.tooltips_reforged.scaleFactor", 1.0).json("scaleFactor");
        public final IConfigEntry<Boolean> useNameColor = new BooleanEntry("config.tooltips_reforged.useNameColor", false).json("useNameColor");
        public final IConfigEntry<Integer> backgroundColor = new IntegerEntry("config.tooltips_reforged.backgroundColor", 0xF0100010).json("backgroundColor");
        public final IConfigEntry<Integer> itemBorderColor = new IntegerEntry("config.tooltips_reforged.itemBorderColor", 0xFFAAAAAA).json("itemBorderColor");
        public final IConfigEntry<Boolean> mouseScrollTooltip = new BooleanEntry("config.tooltips_reforged.mouseScrollTooltip", false).json("mouseScrollTooltip");

        public MiscConfig() {
            super("misc", "screen.%s.misc".formatted(TooltipReforgedClient.MOD_ID));
        }
    }
}
