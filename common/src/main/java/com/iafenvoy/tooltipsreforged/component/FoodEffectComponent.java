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
    private static final Identifier FOOD_FULL_TEXTURE = Identifier.ofVanilla("hud/food_full");
    private static final Identifier SATURATION_TEXTURE = Identifier.of(TooltipReforgedClient.MOD_ID, "textures/gui/saturation.png");
    protected final ItemStack stack;
    private final EffectsRenderMode effectsMode;

    public FoodEffectComponent(ItemStack stack) {
        this.stack = stack;
        this.effectsMode = TooltipReforgedConfig.INSTANCE.tooltip.effectsTooltip.getValue();
    }

    public FoodComponent getFoodComponent() {
        return this.stack.getOrDefault(DataComponentTypes.FOOD, FoodComponents.APPLE);
    }

    public float getHunger() {
        return this.getFoodComponent().nutrition();
    }

    public float getSaturation() {
        FoodComponent foodComponent = this.getFoodComponent();
        return foodComponent != null ? foodComponent.saturation() : 0;
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
        if (foodComponent == null) return 0;

        if (TooltipReforgedConfig.INSTANCE.tooltip.hungerTooltip.getValue() && foodComponent.nutrition() > 0)
            hungerLine = textRenderer.getWidth(Text.translatable("tooltip.%s.hunger".formatted(TooltipReforgedClient.MOD_ID))) + 1 + (int) ((textRenderer.fontHeight - 2) * this.getHunger() / 2);
        if (TooltipReforgedConfig.INSTANCE.tooltip.saturationTooltip.getValue() && foodComponent.saturation() > 0)
            saturationLine = textRenderer.getWidth(Text.translatable("tooltip.%s.saturation".formatted(TooltipReforgedClient.MOD_ID))) + 1 + (int) ((textRenderer.fontHeight - 2) * this.getSaturation() / 2);
        foodWidth = Math.max(hungerLine, saturationLine);

        if (!this.effectsMode.shouldRender()) return foodWidth + 4;
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
        int lineY = y;

        if (TooltipReforgedConfig.INSTANCE.tooltip.hungerTooltip.getValue() && foodComponent.nutrition() > 0) {
            float hungerIcons = this.getHunger() / 2;
            Text hungerText = Text.translatable("tooltip.%s.hunger".formatted(TooltipReforgedClient.MOD_ID));
            context.drawText(textRenderer, hungerText, x, lineY, 0xffffffff, true);
            int hungerWidth = textRenderer.getWidth(hungerText) + 1;
            for (; hungerIcons >= 1; hungerIcons--) {
                context.drawGuiTexture(FOOD_FULL_TEXTURE, x + hungerWidth, lineY, 9, 9);
                hungerWidth += textRenderer.fontHeight - 2;
            }
            if (hungerIcons > 0)
                context.drawGuiTexture(FOOD_FULL_TEXTURE, 9, 9, 0, 0, x + hungerWidth, lineY, (int) (9 * hungerIcons), 9);
            lineY += textRenderer.fontHeight + 1;
        }

        if (TooltipReforgedConfig.INSTANCE.tooltip.saturationTooltip.getValue() && foodComponent.saturation() > 0) {
            float saturationIcons = this.getSaturation() / 2;
            Text saturationText = Text.translatable("tooltip.%s.saturation".formatted(TooltipReforgedClient.MOD_ID));
            context.drawText(textRenderer, saturationText, x, lineY, 0xffffffff, true);
            int saturationWidth = textRenderer.getWidth(saturationText) + 1;
            for (; saturationIcons >= 1; saturationIcons--) {
                context.drawTexture(SATURATION_TEXTURE, x + saturationWidth, lineY, 0, 0, textRenderer.fontHeight, textRenderer.fontHeight, 9, 9);
                saturationWidth += textRenderer.fontHeight - 2;
            }
            if (saturationIcons > 0)
                context.drawTexture(SATURATION_TEXTURE, x + saturationWidth, lineY, 0, 0, 0, (int) (9 * saturationIcons), 9, 9, 9);
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
