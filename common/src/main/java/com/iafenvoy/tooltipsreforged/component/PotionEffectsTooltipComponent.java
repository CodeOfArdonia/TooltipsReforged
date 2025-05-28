package com.iafenvoy.tooltipsreforged.component;

import com.iafenvoy.tooltipsreforged.config.TooltipReforgedConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.client.texture.Sprite;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffectUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionUtil;
import net.minecraft.text.Text;

import java.util.List;

public class PotionEffectsTooltipComponent implements TooltipComponent {
    private final ItemStack stack;
    private final float durationMultiplier;
    private final TooltipReforgedConfig config;

    public PotionEffectsTooltipComponent(ItemStack stack, float durationMultiplier) {
        this.stack = stack;
        this.durationMultiplier = durationMultiplier;
        this.config = TooltipReforgedConfig.INSTANCE;
    }

    public List<StatusEffectInstance> getPotionEffects() {
        return PotionUtil.getPotionEffects(this.stack);
    }

    @Override
    public int getHeight() {
        return this.getPotionEffects().size() * 10;
    }

    @Override
    public int getWidth(TextRenderer textRenderer) {
        int effectsWidth = 0;
        for (StatusEffectInstance effect : this.getPotionEffects())
            effectsWidth = Math.max(effectsWidth, textRenderer.getWidth(Text.translatable(effect.getTranslationKey()).append(" (99:99)")));
        return effectsWidth + 4;
    }

    @Override
    public void drawItems(TextRenderer textRenderer, int x, int y, DrawContext context) {
        int lineY = y - textRenderer.fontHeight - 1;
        if (!this.config.common.effectsTooltip.getValue()) return;
        for (StatusEffectInstance effect : this.getPotionEffects()) {
            int c = effect.getEffectType().getColor();
            Sprite effectTexture = MinecraftClient.getInstance().getStatusEffectSpriteManager().getSprite(effect.getEffectType());
            Text mutableText = Text.translatable(effect.getTranslationKey());
            if (effect.getAmplifier() > 0)
                mutableText = Text.translatable("potion.withAmplifier", mutableText, Text.translatable("potion.potency." + effect.getAmplifier()));
            if (!effect.isDurationBelow(20))
                mutableText = Text.translatable("potion.withDuration", mutableText, StatusEffectUtil.getDurationText(effect, this.durationMultiplier));
            lineY += textRenderer.fontHeight + 1;
            if (this.config.common.effectsIcon.getValue()) {
                context.drawSprite(x - 2, lineY, 0, textRenderer.fontHeight, textRenderer.fontHeight, effectTexture);
                context.drawText(textRenderer, mutableText, x + textRenderer.fontHeight + 2, lineY, c, true);
            } else
                context.drawText(textRenderer, mutableText, x, lineY, c, true);
        }
    }
}
