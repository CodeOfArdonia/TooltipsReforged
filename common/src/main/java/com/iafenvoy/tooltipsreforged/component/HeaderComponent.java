package com.iafenvoy.tooltipsreforged.component;

import com.iafenvoy.tooltipsreforged.config.TooltipReforgedConfig;
import com.iafenvoy.tooltipsreforged.render.TooltipProviders;
import com.iafenvoy.tooltipsreforged.render.TooltipsRenderHelper;
import com.iafenvoy.tooltipsreforged.util.BadgesUtils;
import com.mojang.datafixers.util.Pair;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import org.joml.Matrix4f;

@Environment(EnvType.CLIENT)
public class HeaderComponent implements TooltipComponent {
    private static final int TEXTURE_SIZE = 20, ITEM_MODEL_SIZE = 16, SPACING = 4;
    private final ItemStack stack;
    private final OrderedText nameText, rarityName;
    private final Pair<Text, Integer> badgePair;

    public HeaderComponent(ItemStack stack) {
        this.stack = stack;
        this.nameText = TooltipProviders.getDisplayName(stack).asOrderedText();
        this.rarityName = TooltipProviders.getRarityName(stack).asOrderedText();
        this.badgePair = BadgesUtils.getBadgeText(this.stack);
    }

    @Override
    public int getHeight() {
        return TEXTURE_SIZE + 2;
    }

    @Override
    public int getWidth(TextRenderer textRenderer) {
        int rarityWidth = 0, badgeWidth = 0, titleWidth;
        if (TooltipReforgedConfig.INSTANCE.tooltip.rarityTooltip.getValue())
            rarityWidth = textRenderer.getWidth(this.rarityName);
        Text badgeText = this.badgePair.getFirst();
        if (TooltipReforgedConfig.INSTANCE.tooltip.itemGroupTooltip.getValue())
            badgeWidth = textRenderer.getWidth(badgeText) + SPACING * 2;
        if (TooltipReforgedConfig.INSTANCE.tooltip.rarityTooltip.getValue())
            titleWidth = textRenderer.getWidth(this.nameText) + badgeWidth + 2;
        else titleWidth = Math.max(textRenderer.getWidth(this.nameText), badgeWidth);
        return Math.max(titleWidth, rarityWidth) + this.getTitleOffset() + (this.getTitleOffset() - TEXTURE_SIZE) / 2 - 3;
    }

    public int getTitleOffset() {
        return SPACING + TEXTURE_SIZE;
    }

    @Override
    public void drawText(TextRenderer textRenderer, int x, int y, Matrix4f matrix, VertexConsumerProvider.Immediate vertexConsumers) {
        float startDrawX = (float) x + this.getTitleOffset();
        float startDrawY = y + 1;
        textRenderer.draw(this.nameText, startDrawX, startDrawY - 1, -1, true, matrix, vertexConsumers, TextRenderer.TextLayerType.NORMAL, 0, 0xF000F0);

        if (TooltipReforgedConfig.INSTANCE.tooltip.rarityTooltip.getValue()) {
            startDrawY += textRenderer.fontHeight + 2;
            textRenderer.draw(this.rarityName, startDrawX, startDrawY, -1, true, matrix, vertexConsumers, TextRenderer.TextLayerType.NORMAL, 0, 0xF000F0);
        }
    }

    @Override
    public void drawItems(TextRenderer textRenderer, int x, int y, DrawContext context) {
        int startDrawX = x + (TEXTURE_SIZE - ITEM_MODEL_SIZE) / 2;
        int startDrawY = y + (TEXTURE_SIZE - ITEM_MODEL_SIZE) / 2;
        context.drawItem(this.stack, startDrawX, startDrawY);
        int color = TooltipReforgedConfig.INSTANCE.misc.itemBorderColor.getValue();
        TooltipsRenderHelper.renderVerticalLine(context, startDrawX - 3, startDrawY - 2, 20, 21, color);
        TooltipsRenderHelper.renderVerticalLine(context, startDrawX + 18, startDrawY - 2, 20, 0, color);
        TooltipsRenderHelper.renderHorizontalLine(context, startDrawX - 2, startDrawY - 3, 20, 0, color);
        TooltipsRenderHelper.renderHorizontalLine(context, startDrawX - 2, startDrawY + 18, 20, 0, color);

        if (!TooltipReforgedConfig.INSTANCE.tooltip.itemGroupTooltip.getValue()) return;
        this.drawBadge(textRenderer, this.badgePair.getFirst(), x, y - 2, context, this.badgePair.getSecond());
    }

    private void drawBadge(TextRenderer textRenderer, Text text, int x, int y, DrawContext context, int fillColor) {
        int textWidth = textRenderer.getWidth(text);
        int textHeight = textRenderer.fontHeight - 2;
        int textX = x + this.getTitleOffset() + (TooltipReforgedConfig.INSTANCE.tooltip.rarityTooltip.getValue() ? textRenderer.getWidth(this.nameText) + SPACING + 2 : 4);
        int textY = (TooltipReforgedConfig.INSTANCE.tooltip.rarityTooltip.getValue() ? y : y + 12) - textRenderer.fontHeight + SPACING * 2 + 4;
        context.fill(textX - SPACING, textY - SPACING / 2, textX + textWidth + SPACING, textY + textHeight, BadgesUtils.darkenColor(fillColor, 0.9f));
        context.drawText(textRenderer, text, textX, textY - 1, 0xffffffff, true);
        BadgesUtils.drawFrame(context, textX - SPACING, textY - SPACING / 2, textWidth + SPACING * 2, textHeight + SPACING, 400, BadgesUtils.darkenColor(fillColor, 0.8f));
    }
}
