package com.iafenvoy.tooltipsreforged.render;

import com.iafenvoy.tooltipsreforged.TooltipReforgedClient;
import com.iafenvoy.tooltipsreforged.config.TooltipReforgedConfig;
import com.iafenvoy.tooltipsreforged.render.api.ItemBorderColorProvider;
import com.iafenvoy.tooltipsreforged.render.api.ItemDisplayNameProvider;
import com.iafenvoy.tooltipsreforged.render.api.ItemRarityNameProvider;
import com.iafenvoy.tooltipsreforged.util.ComponentUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;

public class TooltipProviders {
    static ItemRarityNameProvider rarityNameProvider = new DefaultItemRarityNameProvider();
    static ItemDisplayNameProvider displayNameProvider = new DefaultItemDisplayNameProvider();
    static ItemBorderColorProvider borderColorProvider = new DefaultItemBorderColorProvider();

    public static Text getRarityName(ItemStack stack) {
        return rarityNameProvider.getRarityName(stack);
    }

    public static Text getDisplayName(ItemStack stack) {
        return displayNameProvider.getDisplayName(stack);
    }

    public static int getItemBorderColor(ItemStack stack) {
        return borderColorProvider.getItemBorderColor(stack);
    }

    private static class DefaultItemRarityNameProvider implements ItemRarityNameProvider {
        @Override
        public Text getRarityName(ItemStack stack) {
            return Text.translatable("rarity.%s.%s".formatted(TooltipReforgedClient.MOD_ID, stack.getRarity().name().toLowerCase())).setStyle(Style.EMPTY.withColor(Colors.GRAY));
        }
    }

    private static class DefaultItemDisplayNameProvider implements ItemDisplayNameProvider {
        @Override
        public Text getDisplayName(ItemStack stack) {
            return Text.empty().append(stack.getName()).formatted(stack.getRarity().formatting);
        }
    }

    private static class DefaultItemBorderColorProvider implements ItemBorderColorProvider {
        @Override
        public int getItemBorderColor(ItemStack stack) {
            Integer color = null;

            if (TooltipReforgedConfig.INSTANCE.common.useNameColor.getValue()) {
                color = ComponentUtil.getColorFromTranslation(getDisplayName(stack));
            }

            if (color == null || color == -1) {
                color = stack.getRarity().formatting.getColorValue();
                if (color == null || color == 0xFFFFFF) color = 0xFFFFFFFF;
            }

            return color;
        }
    }
}
