package com.iafenvoy.tooltipsreforged.component;

import com.iafenvoy.tooltipsreforged.config.EffectsRenderMode;
import com.iafenvoy.tooltipsreforged.config.TooltipReforgedConfig;
import com.iafenvoy.tooltipsreforged.util.TextUtil;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.client.texture.Sprite;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionUtil;
import net.minecraft.text.Text;

import java.util.List;

@Environment(EnvType.CLIENT)
public class PotionEffectsComponent implements TooltipComponent {
    private final float durationMultiplier;
    private final List<StatusEffectInstance> effects;
    private final EffectsRenderMode effectsMode;

    public PotionEffectsComponent(ItemStack stack, float durationMultiplier) {
        this.durationMultiplier = durationMultiplier;
        this.effects = PotionUtil.getPotionEffects(stack);
        this.effectsMode = (EffectsRenderMode) TooltipReforgedConfig.INSTANCE.tooltip.effectsTooltip.getValue();
    }

    @Override
    public int getHeight() {
        return this.effects.size() * 10;
    }

    @Override
    public int getWidth(TextRenderer textRenderer) {
        int effectsWidth = 0;
        for (StatusEffectInstance effect : this.effects) {
            String text = Text.translatable(effect.getTranslationKey()).getString();
            if (effect.getAmplifier() > 0)
                text = Text.translatable("potion.withAmplifier", text, Text.translatable("potion.potency." + effect.getAmplifier()).getString()).getString();
            if (!effect.isDurationBelow(20))
                text += " " + TextUtil.getDurationText(effect, this.durationMultiplier).getString();
            effectsWidth = Math.max(effectsWidth, textRenderer.getWidth(text) + 14);
        }
        return effectsWidth + 4;
    }

    @Override
    public void drawItems(TextRenderer textRenderer, int x, int y, DrawContext context) {
        if (!this.effectsMode.shouldRender()) return;
        int lineY = y - textRenderer.fontHeight - 1;
        for (StatusEffectInstance effect : this.effects) {
            int c = effect.getEffectType().getColor();
            if (c == 0) c = 0xFF5454FC;
            Sprite effectTexture = MinecraftClient.getInstance().getStatusEffectSpriteManager().getSprite(effect.getEffectType());
            Text mutableText = Text.translatable(effect.getTranslationKey());
            if (effect.getAmplifier() > 0)
                mutableText = Text.translatable("potion.withAmplifier", mutableText, Text.translatable("potion.potency." + effect.getAmplifier()));
            if (!effect.isDurationBelow(20)) {
                String durationText = TextUtil.getDurationText(effect, this.durationMultiplier).getString();
                mutableText = Text.literal(mutableText.getString() + " (" + durationText + ")");
            }
            lineY += textRenderer.fontHeight + 1;
            if (this.effectsMode.shouldRenderIcon()) {
                context.drawSprite(x - 1, lineY - 1, 0, textRenderer.fontHeight, textRenderer.fontHeight, effectTexture);
                context.drawText(textRenderer, mutableText, x + textRenderer.fontHeight + 2, lineY, c, true);
            } else context.drawText(textRenderer, mutableText, x, lineY, c, true);
        }
    }
}
