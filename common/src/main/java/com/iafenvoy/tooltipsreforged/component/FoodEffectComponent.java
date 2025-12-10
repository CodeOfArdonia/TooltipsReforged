package com.iafenvoy.tooltipsreforged.component;

import com.iafenvoy.tooltipsreforged.TooltipReforgedClient;
import com.iafenvoy.tooltipsreforged.config.TooltipReforgedConfig;
import com.iafenvoy.tooltipsreforged.config.mode.EffectsRenderMode;
import com.iafenvoy.tooltipsreforged.util.TextUtil;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.client.texture.Sprite;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.component.type.FoodComponents;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class FoodEffectComponent implements TooltipComponent {
    private static final Identifier FOOD_HALF_TEXTURE = Identifier.ofVanilla("hud/food_half");
    private static final Identifier FOOD_FULL_TEXTURE = Identifier.ofVanilla("hud/food_full");
    protected final ItemStack stack;
    private final EffectsRenderMode effectsMode;

    public FoodEffectComponent(ItemStack stack) {
        this.stack = stack;
        this.effectsMode = TooltipReforgedConfig.INSTANCE.tooltip.effectsTooltip.getValue();
    }

    public FoodComponent getFoodComponent() {
        return this.stack.getOrDefault(DataComponentTypes.FOOD, FoodComponents.APPLE);
    }

    public int getHunger() {
        return this.getFoodComponent().nutrition();
    }

    public int getSaturation() {
        FoodComponent foodComponent = this.getFoodComponent();
        int saturation = 0;
        if (foodComponent != null)
            saturation = (int) (foodComponent.saturation() * 100);
        return saturation;
    }

    @Override
    public int getHeight() {
        int height = 0;
        FoodComponent foodComponent = this.getFoodComponent();
        if (foodComponent != null) {
            if (TooltipReforgedConfig.INSTANCE.tooltip.hungerTooltip.getValue() && foodComponent.nutrition() > 0)
                height += 10;
            if (TooltipReforgedConfig.INSTANCE.tooltip.saturationTooltip.getValue() && foodComponent.saturation() > 0)
                height += 10;
            if (this.effectsMode.shouldRender())
                height += foodComponent.effects().size() * 10;
        }
        return height;
    }

    @Override
    public int getWidth(TextRenderer textRenderer) {
        int foodWidth, effectsWidth = 0, hungerLine = 0, saturationLine = 0;
        FoodComponent foodComponent = this.getFoodComponent();
        int hunger = this.getHunger();

        if (TooltipReforgedConfig.INSTANCE.tooltip.hungerTooltip.getValue() && foodComponent.nutrition() > 0)
            hungerLine = textRenderer.getWidth(Text.translatable("tooltip.%s.hunger".formatted(TooltipReforgedClient.MOD_ID))) + 1 + ((textRenderer.fontHeight - 2) * hunger);
        if (TooltipReforgedConfig.INSTANCE.tooltip.saturationTooltip.getValue() && foodComponent.saturation() > 0)
            saturationLine = textRenderer.getWidth(Text.translatable("tooltip.%s.saturation".formatted(TooltipReforgedClient.MOD_ID), "100%"));
        foodWidth = Math.max(hungerLine, saturationLine);

        if (!this.effectsMode.shouldRender()) return foodWidth + 4;
        if (foodComponent == null) return 0;
        for (FoodComponent.StatusEffectEntry effect : foodComponent.effects()) {
            StatusEffectInstance statusEffect = effect.effect();
            String text = Text.translatable(statusEffect.getTranslationKey()).getString();
            if (statusEffect.getAmplifier() > 0)
                text = Text.translatable("potion.withAmplifier", text, Text.translatable("potion.potency." + statusEffect.getAmplifier()).getString()).getString();
            if (!statusEffect.isDurationBelow(20))
                text += " " + TextUtil.getDurationText(statusEffect, 1.0f).getString();
            effectsWidth = Math.max(effectsWidth, textRenderer.getWidth(text) + 14);
        }
        return Math.max(foodWidth, effectsWidth) + 4;
    }

    @Override
    public void drawItems(TextRenderer textRenderer, int x, int y, DrawContext context) {
        FoodComponent foodComponent = this.getFoodComponent();
        if (foodComponent == null) return;
        int hunger = this.getHunger();
        Text hungerText = Text.translatable("tooltip.%s.hunger".formatted(TooltipReforgedClient.MOD_ID));
        Text saturationText = Text.translatable("tooltip.%s.saturation".formatted(TooltipReforgedClient.MOD_ID), this.getSaturation());
        int lineY = y;

        if (TooltipReforgedConfig.INSTANCE.tooltip.hungerTooltip.getValue() && foodComponent.nutrition() > 0) {
            context.drawText(textRenderer, hungerText, x, lineY, 0xffffffff, true);
            float fullHungers = hunger / 2f;
            boolean hasHalfHunger = (hunger % 2) != 0;
            int hungerWidth = textRenderer.getWidth(hungerText) + 1;
            for (int i = 0; i < (int) fullHungers; i++) {
                context.drawGuiTexture(FOOD_FULL_TEXTURE, x + hungerWidth, lineY, 9, 9);
                hungerWidth += textRenderer.fontHeight - 2;
            }
            if (hasHalfHunger)
                context.drawGuiTexture(FOOD_HALF_TEXTURE, x + hungerWidth, lineY, 9, 9);
            lineY += textRenderer.fontHeight + 1;
        }

        if (TooltipReforgedConfig.INSTANCE.tooltip.saturationTooltip.getValue() && foodComponent.saturation() > 0) {
            context.drawText(textRenderer, saturationText, x, lineY, 0xff00ffff, true);
            lineY += textRenderer.fontHeight + 1;
        }

        if (!this.effectsMode.shouldRender()) return;
        for (FoodComponent.StatusEffectEntry effect : foodComponent.effects()) {
            StatusEffectInstance statusEffect = effect.effect();
            int c = statusEffect.getEffectType().value().getColor();
            Sprite effectTexture = MinecraftClient.getInstance().getStatusEffectSpriteManager().getSprite(statusEffect.getEffectType());
            if (c == 0) c = 0xFF5454FC;

            String effectName = Text.translatable(statusEffect.getTranslationKey()).getString();
            if (statusEffect.getAmplifier() > 0)
                effectName = Text.translatable("potion.withAmplifier", effectName, Text.translatable("potion.potency." + statusEffect.getAmplifier()).getString()).getString();
            String fullText = effectName;
            if (!statusEffect.isDurationBelow(20))
                fullText += " (" + TextUtil.getDurationText(statusEffect, 1.0f).getString() + ")";
            Text renderText = Text.literal(fullText);
            if (this.effectsMode.shouldRenderIcon()) {
                context.drawSprite(x - 1, lineY - 1, 0, textRenderer.fontHeight, textRenderer.fontHeight, effectTexture);
                context.drawText(textRenderer, renderText, x + textRenderer.fontHeight + 2, lineY, c, true);
            } else context.drawText(textRenderer, renderText, x, lineY, c, true);
            lineY += textRenderer.fontHeight + 1;
        }
    }
}
