package com.iafenvoy.tooltipsreforged.component;

import com.google.common.collect.ImmutableList;
import com.iafenvoy.tooltipsreforged.config.TooltipReforgedConfig;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.entity.BannerPattern;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.client.render.TexturedRenderLayers;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.item.BannerPatternItem;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.BannerPatternTags;

import java.util.List;

@Environment(EnvType.CLIENT)
public class BannerPatternComponent implements TooltipComponent {
    private final SpriteIdentifier texture;

    public BannerPatternComponent(ItemStack stack) {
        this.texture = getPatternsFor(stack).stream().findFirst().map(RegistryEntry::getKey).flatMap(x -> x).map(TexturedRenderLayers::getBannerPatternTextureId).orElse(null);
    }

    @Override
    public int getHeight() {
        return this.texture != null && TooltipReforgedConfig.INSTANCE.common.bannerTooltip.getValue() ? 50 : 0;
    }

    @Override
    public int getWidth(TextRenderer textRenderer) {
        return this.texture != null && TooltipReforgedConfig.INSTANCE.common.bannerTooltip.getValue() ? 20 : 0;
    }

    @Override
    public void drawItems(TextRenderer textRenderer, int x, int y, DrawContext context) {
        if (this.texture == null || !TooltipReforgedConfig.INSTANCE.common.bannerTooltip.getValue()) return;
        context.drawTexture(this.texture.getTextureId().withPrefixedPath("textures/").withSuffixedPath(".png"), x, y, 22, 4244, 0, 0, 22, 42, 64, 64);
    }


    private static List<RegistryEntry<BannerPattern>> getPatternsFor(ItemStack stack) {
        if (stack.isEmpty())
            return Registries.BANNER_PATTERN.getEntryList(BannerPatternTags.NO_ITEM_REQUIRED).map(ImmutableList::copyOf).orElse(ImmutableList.of());
        else if (stack.getItem() instanceof BannerPatternItem pattern)
            return Registries.BANNER_PATTERN.getEntryList(pattern.getPattern()).map(ImmutableList::copyOf).orElse(ImmutableList.of());
        else return List.of();
    }
}
