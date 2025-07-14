package com.iafenvoy.tooltipsreforged.component;

import com.iafenvoy.tooltipsreforged.TooltipReforgedClient;
import com.iafenvoy.tooltipsreforged.config.TooltipReforgedConfig;
import com.iafenvoy.tooltipsreforged.util.BadgesUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Style;
import net.minecraft.text.Text;

@Environment(EnvType.CLIENT)
public class DurabilityComponent implements TooltipComponent {
    private static final int SPACING = 4, WIDTH = 80;
    private final ItemStack stack;

    public DurabilityComponent(ItemStack stack) {
        this.stack = stack;
    }

    public boolean isDurabilityDisabled() {
        return !this.stack.isDamageable() || !TooltipReforgedConfig.INSTANCE.common.durabilityTooltip.getValue();
    }

    private Text getDurabilityText() {
        int damaged = this.stack.getMaxDamage() - this.stack.getDamage();
        if (TooltipReforgedConfig.INSTANCE.common.percentageDurability.getValue()) {
            Text percentageText = Text.literal(" " + (damaged * 100 / this.stack.getMaxDamage()) + "%");
            return TooltipReforgedConfig.INSTANCE.common.durabilityTooltip.getValue() ? percentageText : percentageText.getWithStyle(Style.EMPTY.withColor(this.stack.getItemBarColor())).get(0);
        } else return TooltipReforgedConfig.INSTANCE.common.durabilityTooltip.getValue()
                ? Text.literal(" " + damaged + " / " + this.stack.getMaxDamage())
                : Text.literal(" ")
                .append(Text.literal(String.valueOf(damaged)).setStyle(Style.EMPTY.withColor(this.stack.getItemBarColor())))
                .append(Text.literal(" / ").setStyle(Style.EMPTY.withColor(-8355712)))
                .append(Text.literal(String.valueOf(this.stack.getMaxDamage())).setStyle(Style.EMPTY.withColor(0xFF00FF00)));
    }

    @Override
    public int getHeight() {
        if (this.isDurabilityDisabled()) return 0;
        return TooltipReforgedConfig.INSTANCE.common.durabilityTooltip.getValue() ? 13 : 9;
    }

    @Override
    public int getWidth(TextRenderer textRenderer) {
        if (this.isDurabilityDisabled()) return 0;
        int durabilityTextWidth = textRenderer.getWidth(Text.translatable("tooltip.%s.durability".formatted(TooltipReforgedClient.MOD_ID)));
        if (TooltipReforgedConfig.INSTANCE.common.durabilityTooltip.getValue()) {
            if (TooltipReforgedConfig.INSTANCE.common.durabilityBackground.getValue()) {
                return durabilityTextWidth + SPACING + WIDTH - 5;
            } else {
                Text durability = this.getDurabilityText();
                return durabilityTextWidth + SPACING + textRenderer.getWidth(durability) - 9;
            }
        } else {
            Text durability = this.getDurabilityText();
            return durabilityTextWidth + textRenderer.getWidth(durability);
        }
    }

    @Override
    public void drawItems(TextRenderer textRenderer, int x, int y, DrawContext context) {
        if (this.isDurabilityDisabled()) return;

        if (TooltipReforgedConfig.INSTANCE.common.durabilityTooltip.getValue()) y += 2;
        int textHeight = textRenderer.fontHeight;
        int textY = TooltipReforgedConfig.INSTANCE.common.durabilityTooltip.getValue() ? y - textHeight + SPACING * 2 + 2 : y;
        context.drawText(textRenderer, Text.translatable("tooltip.%s.durability".formatted(TooltipReforgedClient.MOD_ID)), x, textY, 0xffffffff, true);
        x += textRenderer.getWidth(Text.translatable("tooltip.%s.durability".formatted(TooltipReforgedClient.MOD_ID))) + SPACING - 4;

        int damaged = this.stack.getMaxDamage() - this.stack.getDamage();
        Text durabilityText = this.getDurabilityText();
        int color = BadgesUtils.darkenColor(0xff000000 | this.stack.getItemBarColor(), 0.9f);
        boolean enhanced = TooltipReforgedConfig.INSTANCE.common.durabilityTooltip.getValue() && TooltipReforgedConfig.INSTANCE.common.durabilityBackground.getValue();
        if (enhanced) {
            context.fill(x, textY - SPACING / 2 + 1, x + (damaged * WIDTH) / this.stack.getMaxDamage(), textY + textHeight - 1, color);
            BadgesUtils.drawFrame(context, x, textY - SPACING / 2 + 1, WIDTH, textHeight + SPACING - 2, 400, BadgesUtils.darkenColor(0xff000000 | this.stack.getItemBarColor(), 0.8f));
        } else
            durabilityText = durabilityText.copy().setStyle(Style.EMPTY.withColor(color));
        if (!durabilityText.equals(Text.empty())) {
            int textX = enhanced ? x + ((WIDTH - textRenderer.getWidth(durabilityText)) / 2) - 1 : x - 4;
            context.drawText(textRenderer, durabilityText, textX, textY, 0xFFFFFFFF, true);
        }
    }
}
