package com.iafenvoy.tooltipsreforged.config;

import com.iafenvoy.jupiter.config.container.AutoInitConfigContainer;
import com.iafenvoy.jupiter.config.entry.*;
import com.iafenvoy.tooltipsreforged.TooltipReforgedClient;
import com.iafenvoy.tooltipsreforged.config.mode.*;
import com.iafenvoy.tooltipsreforged.util.ComponentsProcessor;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.Locale;

public class TooltipReforgedConfig extends AutoInitConfigContainer {
    public static final TooltipReforgedConfig INSTANCE = new TooltipReforgedConfig();

    public final TooltipConfig tooltip = new TooltipConfig();
    public final MiscConfig misc = new MiscConfig();

    public TooltipReforgedConfig() {
        super(Identifier.of(TooltipReforgedClient.MOD_ID, TooltipReforgedClient.MOD_ID), "screen.%s.title".formatted(TooltipReforgedClient.MOD_ID), "./config/tooltips_reforged.json");
    }

    @SuppressWarnings("unused")
    public static class TooltipConfig extends AutoInitConfigCategoryBase {
        public final EnumEntry<ItemDisplayMode> itemDisplayTooltip = EnumEntry.builder("config.tooltips_reforged.itemDisplayTooltip", ItemDisplayMode.HEADER).json("itemDisplayTooltip").nameProvider(mode -> Text.translatable("config.%s.header.item_display.%s".formatted(TooltipReforgedClient.MOD_ID, mode.name().toLowerCase(Locale.ROOT)))).build();
        public final BooleanEntry rarityTooltip = BooleanEntry.builder("config.tooltips_reforged.rarityTooltip", false).json("rarityTooltip").build();
        public final BooleanEntry itemGroupTooltip = BooleanEntry.builder("config.tooltips_reforged.itemGroupTooltip", true).json("itemGroupTooltip").build();
        public final SeparatorEntry s1 = SeparatorEntry.builder().build();
        public final BooleanEntry hungerTooltip = BooleanEntry.builder("config.tooltips_reforged.hungerTooltip", true).json("hungerTooltip").build();
        public final BooleanEntry saturationTooltip = BooleanEntry.builder("config.tooltips_reforged.saturationTooltip", true).json("saturationTooltip").build();
        public final EnumEntry<EffectsRenderMode> effectsTooltip = EnumEntry.builder("config.tooltips_reforged.effectsTooltip", EffectsRenderMode.WITH_ICON).json("effectsTooltip").nameProvider(mode -> Text.translatable("config.%s.effects.render_mode.%s".formatted(TooltipReforgedClient.MOD_ID, mode.name().toLowerCase(Locale.ROOT)))).build();
        public final SeparatorEntry s2 = SeparatorEntry.builder().build();
        public final EnumEntry<EnchantmentsRenderMode> enchantmentTooltip = EnumEntry.builder("config.tooltips_reforged.enchantmentTooltip", EnchantmentsRenderMode.SHIFT_DETAIL).json("enchantmentTooltip").nameProvider(mode -> Text.translatable("config.%s.enchantments.render_mode.%s".formatted(TooltipReforgedClient.MOD_ID, mode.name().toLowerCase(Locale.ROOT)))).build();
        public final EnumEntry<ArmorRenderMode> armorTooltip = EnumEntry.builder("config.tooltips_reforged.armorTooltip", ArmorRenderMode.PLAYER).json("armorTooltip").nameProvider(mode -> Text.translatable("config.%s.armor.render_mode.%s".formatted(TooltipReforgedClient.MOD_ID, mode.name().toLowerCase(Locale.ROOT)))).build();
        public final BooleanEntry bucketTooltip = BooleanEntry.builder("config.tooltips_reforged.bucketTooltip", true).json("bucketTooltip").build();
        public final BooleanEntry spawnEggTooltip = BooleanEntry.builder("config.tooltips_reforged.spawnEggTooltip", true).json("spawnEggTooltip").build();
        public final EnumEntry<DurabilityRenderMode> durabilityTooltip = EnumEntry.builder("config.tooltips_reforged.durabilityTooltip", DurabilityRenderMode.VANILLA_BACKGROUND).json("durabilityTooltip").nameProvider(mode -> Text.translatable("config.%s.durability.render_mode.%s".formatted(TooltipReforgedClient.MOD_ID, mode.name().toLowerCase(Locale.ROOT)))).build();
        public final BooleanEntry equipmentCompareTooltip = BooleanEntry.builder("config.tooltips_reforged.equipmentCompareTooltip", false).json("equipmentCompareTooltip").build();
        public final SeparatorEntry s3 = SeparatorEntry.builder().build();
        public final EnumEntry<ContainerPreviewRenderMode> containerTooltip = EnumEntry.builder("config.tooltips_reforged.containerTooltip", ContainerPreviewRenderMode.IMAGE).json("containerTooltip").nameProvider(mode -> Text.translatable("config.%s.container.render_mode.%s".formatted(TooltipReforgedClient.MOD_ID, mode.name().toLowerCase(Locale.ROOT)))).build();
        public final BooleanEntry paintingTooltip = BooleanEntry.builder("config.tooltips_reforged.paintingTooltip", true).json("paintingTooltip").build();
        public final BooleanEntry bannerTooltip = BooleanEntry.builder("config.tooltips_reforged.bannerTooltip", true).json("bannerTooltip").build();
        public final BooleanEntry mapTooltip = BooleanEntry.builder("config.tooltips_reforged.mapTooltip", true).json("mapTooltip").build();
        public final SeparatorEntry s4 = SeparatorEntry.builder().build();
        public final BooleanEntry debugInfoTooltip = BooleanEntry.builder("config.tooltips_reforged.debugInfoTooltip", true).json("debugInfoTooltip").build();

        public TooltipConfig() {
            super("common", "screen.%s.tooltip".formatted(TooltipReforgedClient.MOD_ID));
        }
    }

    @SuppressWarnings("unused")
    public static class MiscConfig extends AutoInitConfigCategoryBase {
        public final ListStringEntry blacklist = ListStringEntry.builder("config.tooltips_reforged.blacklist", List.of()).json("blacklist").build();
        public final ListStringEntry ignoredComponents = ListStringEntry.builder("config.tooltips_reforged.ignoredComponents", ComponentsProcessor.DEFAULT_IGNORED).json("ignoredComponents").build();
        public final SeparatorEntry s1 = SeparatorEntry.builder().build();
        public final BooleanEntry overwriteRarityColor = BooleanEntry.builder("config.tooltips_reforged.overwriteRarityColor", false).json("overwriteRarityColor").build();
        public final IntegerEntry commonRarityColor = IntegerEntry.builder("config.tooltips_reforged.commonRarityColor", 0x505000FF).json("commonRarityColor").build();
        public final IntegerEntry uncommonRarityColor = IntegerEntry.builder("config.tooltips_reforged.uncommonRarityColor", 0xFFFFFF55).json("uncommonRarityColor").build();
        public final IntegerEntry rareRarityColor = IntegerEntry.builder("config.tooltips_reforged.rareRarityColor", 0xFF55FFFF).json("rareRarityColor").build();
        public final IntegerEntry epicRarityColor = IntegerEntry.builder("config.tooltips_reforged.epicRarityColor", 0xFFFF00FF).json("epicRarityColor").build();
        public final IntegerEntry endColor = IntegerEntry.builder("config.tooltips_reforged.endColor", 0x505000FF).json("endColor").build();
        public final SeparatorEntry s2 = SeparatorEntry.builder().build();
        public final BooleanEntry useNameColor = BooleanEntry.builder("config.tooltips_reforged.useNameColor", false).json("useNameColor").build();
        public final BooleanEntry useOriginalTitle = BooleanEntry.builder("config.tooltips_reforged.useOriginalTitle", false).json("useOriginalTitle").build();
        public final BooleanEntry useImageBackground = BooleanEntry.builder("config.tooltips_reforged.useImageBackground", false).json("useImageBackground").build();
        public final BooleanEntry useImageBorder = BooleanEntry.builder("config.tooltips_reforged.useImageBorder", false).json("useImageBorder").build();
        public final IntegerEntry backgroundColor = IntegerEntry.builder("config.tooltips_reforged.backgroundColor", 0xF0100010).json("backgroundColor").build();
        public final IntegerEntry itemBorderColor = IntegerEntry.builder("config.tooltips_reforged.itemBorderColor", 0xFFAAAAAA).json("itemBorderColor").build();
        public final BooleanEntry mouseScrollTooltip = BooleanEntry.builder("config.tooltips_reforged.mouseScrollTooltip", false).json("mouseScrollTooltip").build();
        public final SeparatorEntry s3 = SeparatorEntry.builder().build();
        public final IntegerEntry itemZoomSize = IntegerEntry.builder("config.tooltips_reforged.itemZoomSize", 100).range(0, 2048).json("itemZoomSize").build();
        public final IntegerEntry itemZoomXOffset = IntegerEntry.builder("config.tooltips_reforged.itemZoomXOffset", 0).json("itemZoomXOffset").build();
        public final IntegerEntry itemZoomYOffset = IntegerEntry.builder("config.tooltips_reforged.itemZoomYOffset", 0).json("itemZoomYOffset").build();
        public final SeparatorEntry s4 = SeparatorEntry.builder().build();
        public final BooleanEntry removeEmptyLines = BooleanEntry.builder("config.tooltips_reforged.removeEmptyLines", true).json("removeEmptyLines").build();
        public final EnumEntry<EnchantmentSortMode> enchantmentSort = EnumEntry.builder("config.tooltips_reforged.enchantmentSort", EnchantmentSortMode.DEFAULT).json("enchantmentSort").nameProvider(mode -> Text.translatable("config.%s.enchantment.sort.%s".formatted(TooltipReforgedClient.MOD_ID, mode.name().toLowerCase(Locale.ROOT)))).build();
        public final BooleanEntry advancedEnchantmentColor = BooleanEntry.builder("config.tooltips_reforged.advancedEnchantmentColor", false).json("advancedEnchantmentColor").build();

        public MiscConfig() {
            super("misc", "screen.%s.misc".formatted(TooltipReforgedClient.MOD_ID));
        }
    }
}
