package com.iafenvoy.tooltipsreforged.component;

import com.iafenvoy.tooltipsreforged.config.TooltipReforgedConfig;
import com.iafenvoy.tooltipsreforged.util.InfoCollectHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

import java.util.Map;
import java.util.Objects;

public class EnchantmentInfoComponent implements TooltipComponent {
    private final Map<Enchantment, Integer> enchantments;

    public EnchantmentInfoComponent(ItemStack stack) {
        this.enchantments = EnchantmentHelper.fromNbt(EnchantedBookItem.getEnchantmentNbt(stack));
    }

    @Override
    public int getHeight() {
        return TooltipReforgedConfig.INSTANCE.common.enchantmentTooltip.getValue() ? this.enchantments.size() * 20 : 0;
    }

    @Override
    public int getWidth(TextRenderer textRenderer) {
        if (!TooltipReforgedConfig.INSTANCE.common.enchantmentTooltip.getValue()) return 0;
        int width = 0;
        for (Map.Entry<Enchantment, Integer> entry : this.enchantments.entrySet()) {
            Enchantment enchantment = entry.getKey();
            width = Math.max(width, textRenderer.getWidth(enchantment.getName(entry.getValue())));
            width = Math.max(width, InfoCollectHelper.resolveEnchantmentTarget(enchantment.target).size() * 12 + textRenderer.getWidth(Objects.requireNonNullElse(Registries.ENCHANTMENT.getId(enchantment), Identifier.of("", "")).toString()));
        }
        return width;
    }

    @Override
    public void drawItems(TextRenderer textRenderer, int x, int y, DrawContext context) {
        if (!TooltipReforgedConfig.INSTANCE.common.enchantmentTooltip.getValue()) return;
        int currentY = y;
        for (Map.Entry<Enchantment, Integer> entry : this.enchantments.entrySet()) {
            Enchantment enchantment = entry.getKey();
            context.drawText(textRenderer, enchantment.getName(entry.getValue()), x, y, -1, true);
            currentY += 10;
            int currentX = x;
            for (Item item : InfoCollectHelper.resolveEnchantmentTarget(enchantment.target)) {
                context.drawItem(new ItemStack(item), currentX, currentY);
                currentX += 12;
            }
            if (MinecraftClient.getInstance().options.advancedItemTooltips)
                context.drawText(textRenderer, Objects.requireNonNullElse(Registries.ENCHANTMENT.getId(enchantment), Identifier.of("", "")).toString(), currentX, currentY, -1, true);
            currentY += 10;
        }
    }
}
