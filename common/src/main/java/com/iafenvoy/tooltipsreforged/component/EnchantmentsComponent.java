package com.iafenvoy.tooltipsreforged.component;

import com.iafenvoy.tooltipsreforged.config.TooltipReforgedConfig;
import com.iafenvoy.tooltipsreforged.config.mode.EnchantmentSortMode;
import com.iafenvoy.tooltipsreforged.config.mode.EnchantmentsRenderMode;
import com.iafenvoy.tooltipsreforged.render.RenderHelper;
import com.iafenvoy.tooltipsreforged.util.RandomHelper;
import com.iafenvoy.tooltipsreforged.util.TextUtil;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.client.util.InputUtil;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.EnchantmentTags;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Environment(EnvType.CLIENT)
public class EnchantmentsComponent implements TooltipComponent, RenderHelper {
    private final List<EnchantmentInfo> enchantments = new LinkedList<>();
    private final EnchantmentsRenderMode mode;
    private final boolean extraColor;

    public EnchantmentsComponent(ItemEnchantmentsComponent component) {
        this.mode = (EnchantmentsRenderMode) TooltipReforgedConfig.INSTANCE.tooltip.enchantmentTooltip.getValue();
        for (Object2IntMap.Entry<RegistryEntry<Enchantment>> entry : component.getEnchantmentEntries()) {
            RegistryEntry<Enchantment> enchantment = entry.getKey();
            String descriptionKey = enchantment.value().description().copy() + ".desc";
            Optional<RegistryKey<Enchantment>> optional = enchantment.getKey();
            if (optional.isEmpty()) continue;
            this.enchantments.add(new EnchantmentInfo(enchantment, entry.getIntValue(), optional.get().getValue(), I18n.hasTranslation(descriptionKey) ? TextUtil.splitText(Text.literal(I18n.translate(descriptionKey)), 300, MinecraftClient.getInstance().textRenderer) : List.of()));
        }
        this.enchantments.sort(((EnchantmentSortMode) TooltipReforgedConfig.INSTANCE.misc.enchantmentSort.getValue()).getComparator());
        this.extraColor = TooltipReforgedConfig.INSTANCE.misc.advancedEnchantmentColor.getValue();
    }

    private static boolean isShiftDown() {
        long handle = MinecraftClient.getInstance().getWindow().getHandle();
        return InputUtil.isKeyPressed(handle, InputUtil.GLFW_KEY_LEFT_SHIFT) || InputUtil.isKeyPressed(handle, InputUtil.GLFW_KEY_RIGHT_SHIFT);
    }

    private boolean shouldDisplayDetail() {
        return this.mode.shouldAlwaysDescription() || isShiftDown();
    }

    @Override
    public int getHeight() {
        boolean includeDetail = this.shouldDisplayDetail();
        return this.mode.shouldRender() ? this.enchantments.stream().reduce(0, (p, c) -> p + c.getHeight(includeDetail), Integer::sum) : 0;
    }

    @Override
    public int getWidth(TextRenderer textRenderer) {
        boolean includeDetail = this.shouldDisplayDetail();
        return this.mode.shouldRender() ? this.enchantments.stream().reduce(0, (p, c) -> Math.max(p, c.getWidth(textRenderer, includeDetail)), Math::max) : 0;
    }

    @Override
    public void drawItems(TextRenderer textRenderer, int x, int y, DrawContext context) {
        if (!this.mode.shouldRender()) return;
        int currentY = y;
        for (EnchantmentInfo info : this.enchantments) {
            int currentX = x;
            Text name = getEnchantmentName(info.enchantment, info.level, this.extraColor);
            context.drawText(textRenderer, name, currentX, currentY, -1, true);
            currentX += textRenderer.getWidth(name) + 2;
            this.drawStack(context, new ItemStack(RandomHelper.pick(info.enchantment.value().definition().supportedItems().stream().toList(), Registries.ITEM.getEntry(Items.AIR))), currentX, currentY, 10);
            currentX += 12;
            if (MinecraftClient.getInstance().options.advancedItemTooltips)
                context.drawText(textRenderer, info.id.toString(), currentX, currentY, 5592405, true);
            currentY += 10;
            if (this.shouldDisplayDetail())
                for (MutableText text : info.descriptions) {
                    context.drawText(textRenderer, text.formatted(Formatting.DARK_GRAY), x, currentY, -1, true);
                    currentY += 10;
                }
        }
    }

    private static Text getEnchantmentName(RegistryEntry<Enchantment> enchantment, int level, boolean extraColor) {
        MutableText mutableText = enchantment.value().description().copy();
        if (enchantment.isIn(EnchantmentTags.CURSE)) mutableText.formatted(Formatting.RED);
        else if (extraColor && enchantment.value().getMaxLevel() < level)
            mutableText.formatted(Formatting.LIGHT_PURPLE);
        else if (extraColor) mutableText.formatted(Formatting.GREEN);
        else mutableText.formatted(Formatting.GRAY);
        if (level != 1 || enchantment.value().getMaxLevel() != 1)
            mutableText.append(ScreenTexts.SPACE).append(Text.translatable("enchantment.level." + level)).append(Text.literal("/").append(Text.translatable("enchantment.level." + enchantment.value().getMaxLevel())).formatted(Formatting.DARK_GRAY));
        return mutableText;
    }

    public record EnchantmentInfo(RegistryEntry<Enchantment> enchantment, int level, Identifier id,
                                  List<MutableText> descriptions) {
        public int getWidth(TextRenderer textRenderer, boolean includeDetail) {
            int width = textRenderer.getWidth(getEnchantmentName(this.enchantment, this.level, false)) + 14 + (MinecraftClient.getInstance().options.advancedItemTooltips ? textRenderer.getWidth(this.id.toString()) : 0);
            if (includeDetail)
                for (MutableText text : this.descriptions) width = Math.max(width, textRenderer.getWidth(text));
            return width;
        }

        public int getHeight(boolean includeDetail) {
            return 10 + (includeDetail ? this.descriptions.size() * 10 : 0);
        }
    }
}
