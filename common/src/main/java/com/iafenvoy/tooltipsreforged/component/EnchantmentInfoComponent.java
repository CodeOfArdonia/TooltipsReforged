package com.iafenvoy.tooltipsreforged.component;

import com.iafenvoy.tooltipsreforged.config.TooltipReforgedConfig;
import com.iafenvoy.tooltipsreforged.util.ComponentUtil;
import com.iafenvoy.tooltipsreforged.util.InfoCollectHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.Registries;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class EnchantmentInfoComponent implements TooltipComponent {
    private final List<EnchantmentInfo> enchantments = new LinkedList<>();

    public EnchantmentInfoComponent(NbtList list) {
        this(EnchantmentHelper.fromNbt(list));
    }

    public EnchantmentInfoComponent(Map<Enchantment, Integer> map) {
        for (Map.Entry<Enchantment, Integer> entry : map.entrySet()) {
            Enchantment enchantment = entry.getKey();
            String descriptionKey = enchantment.getTranslationKey() + ".desc";
            this.enchantments.add(new EnchantmentInfo(enchantment, entry.getValue(), I18n.hasTranslation(descriptionKey) ? ComponentUtil.splitText(Text.literal(I18n.translate(descriptionKey)), 300, MinecraftClient.getInstance().textRenderer) : List.of()));
        }
    }

    @Override
    public int getHeight() {
        return TooltipReforgedConfig.INSTANCE.common.enchantmentTooltip.getValue() ? this.enchantments.stream().reduce(0, (p, c) -> p + c.getHeight(), Integer::sum) : 0;
    }

    @Override
    public int getWidth(TextRenderer textRenderer) {
        return TooltipReforgedConfig.INSTANCE.common.enchantmentTooltip.getValue() ? this.enchantments.stream().reduce(0, (p, c) -> Math.max(p, c.getWidth(textRenderer)), Math::max) : 0;
    }

    @Override
    public void drawItems(TextRenderer textRenderer, int x, int y, DrawContext context) {
        if (!TooltipReforgedConfig.INSTANCE.common.enchantmentTooltip.getValue()) return;
        int currentY = y;
        for (EnchantmentInfo info : this.enchantments) {
            int currentX = x;
            Text name = info.enchantment.getName(info.level);
            context.drawText(textRenderer, name, currentX, currentY + 3, -1, true);
            currentX += textRenderer.getWidth(name) + 4;
            for (Item item : info.targets) {
                context.drawItem(new ItemStack(item), currentX, currentY);
                currentX += 20;
            }
            if (MinecraftClient.getInstance().options.advancedItemTooltips)
                context.drawText(textRenderer, info.id.toString(), currentX, currentY + 3, -1, true);
            currentY += 16;
            for (MutableText text : info.descriptions) {
                context.drawText(textRenderer, text.formatted(Formatting.DARK_GRAY), x, currentY, -1, true);
                currentY += 10;
            }
        }
    }

    private record EnchantmentInfo(Enchantment enchantment, int level, Identifier id, List<Item> targets,
                                   List<MutableText> descriptions) {
        public EnchantmentInfo(Enchantment enchantment, int level, List<MutableText> description) {
            this(enchantment, level, Objects.requireNonNullElse(Registries.ENCHANTMENT.getId(enchantment), Identifier.of("", "")), InfoCollectHelper.getEnchantmentTarget(enchantment.target), description);
        }

        public int getWidth(TextRenderer textRenderer) {
            int width = textRenderer.getWidth(this.enchantment.getName(this.level)) + 4 + this.targets.size() * 20 + (MinecraftClient.getInstance().options.advancedItemTooltips ? textRenderer.getWidth(this.id.toString()) : 0);
            for (MutableText text : this.descriptions) width = Math.max(width, textRenderer.getWidth(text));
            return width;
        }

        public int getHeight() {
            return 16 + this.descriptions.size() * 10;
        }
    }
}
