package com.iafenvoy.tooltipsreforged.component;

import com.iafenvoy.tooltipsreforged.TooltipReforgedClient;
import com.iafenvoy.tooltipsreforged.config.TooltipReforgedConfig;
import com.mojang.datafixers.util.Pair;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.client.texture.Sprite;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffectUtil;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class FoodEffectComponent implements TooltipComponent {
    private final ItemStack stack;

    public FoodEffectComponent(ItemStack stack) {
        this.stack = stack;
    }

    public FoodComponent getFoodComponent() {
        return this.stack.getItem().getFoodComponent();
    }

    public int getHunger() {
        return this.getFoodComponent().getHunger();
    }

    public int getSaturation() {
        FoodComponent foodComponent = this.getFoodComponent();
        int saturation = 0;
        if (foodComponent != null)
            saturation = (int) (foodComponent.getSaturationModifier() * 100);
        return saturation;
    }

    @Override
    public int getHeight() {
        int height = 0;
        FoodComponent foodComponent = this.getFoodComponent();
        if (foodComponent != null) {
            if (TooltipReforgedConfig.INSTANCE.common.hungerTooltip.getValue() && foodComponent.getHunger() > 0)
                height += 10;
            if (TooltipReforgedConfig.INSTANCE.common.saturationTooltip.getValue() && foodComponent.getSaturationModifier() > 0)
                height += 10;
            if (!TooltipReforgedConfig.INSTANCE.common.effectsTooltip.getValue())
                return height;
            height += foodComponent.getStatusEffects().size() * 10;
        }
        return height;
    }

    @Override
    public int getWidth(TextRenderer textRenderer) {
        int foodWidth, effectsWidth = 0, hungerLine = 0, saturationLine = 0;
        FoodComponent foodComponent = this.getFoodComponent();
        int hunger = this.getHunger();

        if (TooltipReforgedConfig.INSTANCE.common.hungerTooltip.getValue() && foodComponent.getHunger() > 0)
            hungerLine = textRenderer.getWidth(Text.translatable("tooltip.%s.hunger".formatted(TooltipReforgedClient.MOD_ID))) + 1 + ((textRenderer.fontHeight - 2) * hunger);
        if (TooltipReforgedConfig.INSTANCE.common.saturationTooltip.getValue() && foodComponent.getSaturationModifier() > 0)
            saturationLine = textRenderer.getWidth(Text.translatable("tooltip.%s.saturation".formatted(TooltipReforgedClient.MOD_ID), "100%"));
        foodWidth = Math.max(hungerLine, saturationLine);

        if (!TooltipReforgedConfig.INSTANCE.common.effectsTooltip.getValue()) return foodWidth + 4;
        if (foodComponent == null) return 0;
        for (Pair<StatusEffectInstance, Float> effect : foodComponent.getStatusEffects())
            effectsWidth = Math.max(effectsWidth, textRenderer.getWidth(Text.translatable(effect.getFirst().getTranslationKey()).append(" (99:99)")));
        return Math.max(foodWidth, effectsWidth) + 4;
    }

    @Override
    public void drawItems(TextRenderer textRenderer, int x, int y, DrawContext context) {
        FoodComponent foodComponent = this.getFoodComponent();
        int hunger = this.getHunger();
        int saturation = this.getSaturation();

        if (foodComponent == null) return;

        Text hungerText = Text.translatable("tooltip.%s.hunger".formatted(TooltipReforgedClient.MOD_ID));
        Text saturationText = Text.translatable("tooltip.%s.saturation".formatted(TooltipReforgedClient.MOD_ID), saturation);

        int lineY = y;

        if (TooltipReforgedConfig.INSTANCE.common.hungerTooltip.getValue()) {
            context.drawText(textRenderer, hungerText, x, lineY, 0xffffffff, true);
            float fullHungers = hunger / 2f;
            boolean hasHalfHunger = (hunger % 2) != 0;
            int hungerWidth = textRenderer.getWidth(hungerText) + 1;
            for (int i = 0; i < (int) fullHungers; i++) {
                context.drawTexture(new Identifier("textures/gui/icons.png"), x + hungerWidth, lineY, 52, 27, textRenderer.fontHeight, textRenderer.fontHeight, 256, 256);
                hungerWidth += textRenderer.fontHeight - 2;
            }
            if (hasHalfHunger)
                context.drawTexture(new Identifier("textures/gui/icons.png"), x + hungerWidth, lineY, 61, 27, textRenderer.fontHeight, textRenderer.fontHeight, 256, 256);
            lineY += textRenderer.fontHeight + 1;
        }

        if (TooltipReforgedConfig.INSTANCE.common.saturationTooltip.getValue())
            context.drawText(textRenderer, saturationText, x, lineY, 0xff00ffff, true);
        else lineY -= textRenderer.fontHeight + 1;

        if (!TooltipReforgedConfig.INSTANCE.common.effectsTooltip.getValue()) return;
        for (Pair<StatusEffectInstance, Float> effect : foodComponent.getStatusEffects()) {
            StatusEffectInstance statusEffect = effect.getFirst();
            int c = statusEffect.getEffectType().getColor();
            Sprite effectTexture = MinecraftClient.getInstance().getStatusEffectSpriteManager().getSprite(statusEffect.getEffectType());

            Text effectText = Text.translatable(statusEffect.getTranslationKey()).append(" (").append(StatusEffectUtil.getDurationText(statusEffect, 1.0f)).append(")");

            lineY += textRenderer.fontHeight + 1;

            if (TooltipReforgedConfig.INSTANCE.common.effectsIcon.getValue()) {
                context.drawSprite(x - 1, lineY - 1, 0, textRenderer.fontHeight, textRenderer.fontHeight, effectTexture);
                context.drawText(textRenderer, effectText, x + textRenderer.fontHeight + 2, lineY, c, true);
            } else
                context.drawText(textRenderer, effectText, x, lineY, c, true);
        }
    }
}
