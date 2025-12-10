package com.iafenvoy.tooltipsreforged.component;

import com.iafenvoy.tooltipsreforged.TooltipReforgedClient;
import com.iafenvoy.tooltipsreforged.config.TooltipReforgedConfig;
import com.iafenvoy.tooltipsreforged.config.mode.DurabilityRenderMode;
import com.iafenvoy.tooltipsreforged.render.RenderHelper;
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
public class DurabilityComponent implements TooltipComponent, RenderHelper {
    private static final int WIDTH = 80;
    protected final ItemStack stack;
    private final DurabilityRenderMode mode;
    private final boolean enabled;
    private final int color;
    private final Text text;

    public DurabilityComponent(ItemStack stack) {
        this.stack = stack;
        this.mode = TooltipReforgedConfig.INSTANCE.tooltip.durabilityTooltip.getValue();
        this.enabled = this.mode.isEnabled() && this.stack.isDamageable() && this.stack.getMaxDamage() > 0;
        this.color = stack.getItemBarColor();
        this.text = this.enabled ? getDurabilityText(stack, this.mode) : Text.literal("");
    }

    private static Text getDurabilityText(ItemStack stack, DurabilityRenderMode mode) {
        int maxDamage = stack.getMaxDamage(), damaged = maxDamage - stack.getDamage();
        if (mode.shouldInPercentage() && maxDamage > 0) {
            Text percentageText = Text.literal(" " + (damaged * 100 / maxDamage) + "%");
            return mode.shouldColorText() ? percentageText.getWithStyle(Style.EMPTY.withColor(stack.getItemBarColor())).get(0) : percentageText;
        } else return mode.shouldColorText() ? Text.literal(" ")
                .append(Text.literal(String.valueOf(damaged)).setStyle(Style.EMPTY.withColor(stack.getItemBarColor())))
                .append(Text.literal(" / ").setStyle(Style.EMPTY.withColor(-8355712)))
                .append(Text.literal(String.valueOf(maxDamage)).setStyle(Style.EMPTY.withColor(0xFF00FF00)))
                : Text.literal(" " + damaged + " / " + maxDamage);
    }

    @Override
    public int getHeight() {
        return this.enabled ? this.mode.shouldRenderBackground() ? 13 : 9 : 0;
    }

    @Override
    public int getWidth(TextRenderer textRenderer) {
        if (!this.enabled) return 0;
        int width = textRenderer.getWidth(Text.translatable("tooltip.%s.durability".formatted(TooltipReforgedClient.MOD_ID)));
        if (this.mode.shouldRenderBackground()) return width + SPACING + WIDTH - 5;
        else return width + textRenderer.getWidth(this.text);
    }

    @Override
    public void drawItems(TextRenderer textRenderer, int x, int y, DrawContext context) {
        if (!this.enabled) return;
        boolean background = this.mode.shouldRenderBackground();
        if (background) y += 2;
        int textHeight = textRenderer.fontHeight;
        int textY = background ? y - textHeight + SPACING * 2 + 2 : y;
        Text durability = Text.translatable("tooltip.%s.durability".formatted(TooltipReforgedClient.MOD_ID));
        context.drawText(textRenderer, durability, x, textY, 0xffffffff, true);
        x += textRenderer.getWidth(durability) + SPACING - 4;
        int damaged = this.stack.getMaxDamage() - this.stack.getDamage();
        if (background) {
            int color = BadgesUtils.darkenColor(0xff000000 | this.color, 0.9f);
            context.fill(x, textY - SPACING / 2 + 1, x + (damaged * WIDTH) / this.stack.getMaxDamage(), textY + textHeight - 1, color);
            BadgesUtils.drawFrame(context, x, textY - SPACING / 2 + 1, WIDTH, textHeight + SPACING - 2, 400, BadgesUtils.darkenColor(0xff000000 | this.stack.getItemBarColor(), 0.8f));
        }
        int textX = background ? x + ((WIDTH - textRenderer.getWidth(this.text)) / 2) - 1 : x - 4;
        context.drawText(textRenderer, this.text, textX, textY, 0xFFFFFFFF, true);
    }

}
