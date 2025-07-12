package com.iafenvoy.tooltipsreforged.render;

import com.iafenvoy.tooltipsreforged.TooltipReforgedClient;
import com.iafenvoy.tooltipsreforged.config.TooltipReforgedConfig;
import com.iafenvoy.tooltipsreforged.util.TextUtil;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;
import net.minecraft.util.Formatting;
import net.minecraft.util.Rarity;

@Environment(EnvType.CLIENT)
public class TooltipProviders {
    public static Text getRarityName(ItemStack stack) {
        return Text.translatable("rarity.%s.%s".formatted(TooltipReforgedClient.MOD_ID, stack.getRarity().name().toLowerCase())).setStyle(Style.EMPTY.withColor(Colors.GRAY));
    }

    public static Text getDisplayName(ItemStack stack) {
        Rarity rarity = stack.getRarity();
        return Text.empty().formatted(rarity.formatting == Formatting.BLACK ? Formatting.WHITE : rarity.formatting).append(stack.getName());
    }

    public static int getItemBorderColor(ItemStack stack) {
        Integer color = null;
        if (TooltipReforgedConfig.INSTANCE.common.useNameColor.getValue())
            color = TextUtil.getColorFromTranslation(getDisplayName(stack));
        if (color == null || color == -1) {
            color = stack.getRarity().formatting.getColorValue();
            if (color == null || color == 0xFFFFFF) color = 0xFFFFFFFF;
        }
        return color;
    }
}
