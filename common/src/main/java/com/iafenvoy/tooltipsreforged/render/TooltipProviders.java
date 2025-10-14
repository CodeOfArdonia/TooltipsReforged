package com.iafenvoy.tooltipsreforged.render;

import com.iafenvoy.tooltipsreforged.TooltipReforgedClient;
import com.iafenvoy.tooltipsreforged.config.TooltipReforgedConfig;
import com.iafenvoy.tooltipsreforged.hook.RarityHook;
import com.iafenvoy.tooltipsreforged.util.TextUtil;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.util.Colors;
import net.minecraft.util.Rarity;

@Environment(EnvType.CLIENT)
public class TooltipProviders {
    public static Text getRarityName(ItemStack stack) {
        return Text.translatable("rarity.%s.%s".formatted(TooltipReforgedClient.MOD_ID, stack.getRarity().name().toLowerCase())).setStyle(Style.EMPTY.withColor(Colors.GRAY));
    }

    public static Text getDisplayName(ItemStack stack) {
        return RarityHook.applyColor(Text.empty(), stack.getRarity()).append(stack.getName());
    }

    public static int getItemBorderColor(ItemStack stack) {
        Integer color = null;
        if (TooltipReforgedConfig.INSTANCE.misc.useNameColor.getValue())
            color = TextUtil.getColorFromTranslation(getDisplayName(stack));
        if (color == null || color == -1) {
            Rarity rarity = stack.getRarity();
            TextColor textColor = RarityHook.applyColor(Text.empty(), rarity).getStyle().getColor();
            if (textColor != null) color = textColor.getRgb();
            else color = rarity.getFormatting().getColorValue();
            if (color == null || color == 0xFFFFFF) color = 0xFFFFFFFF;
        }
        return color;
    }
}
