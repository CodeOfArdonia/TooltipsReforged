package com.iafenvoy.tooltipsreforged.component;

import com.iafenvoy.tooltipsreforged.config.TooltipReforgedConfig;
import com.iafenvoy.tooltipsreforged.util.TextUtil;
import com.iafenvoy.tooltipsreforged.util.InfoCollectHelper;
import com.iafenvoy.tooltipsreforged.util.RandomHelper;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.client.render.DiffuseLighting;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.Registries;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Environment(EnvType.CLIENT)
public class EnchantmentsComponent implements TooltipComponent {
    private final List<EnchantmentInfo> enchantments = new LinkedList<>();

    public EnchantmentsComponent(NbtList list) {
        this(EnchantmentHelper.fromNbt(list));
    }

    public EnchantmentsComponent(Map<Enchantment, Integer> map) {
        for (Map.Entry<Enchantment, Integer> entry : map.entrySet()) {
            Enchantment enchantment = entry.getKey();
            String descriptionKey = enchantment.getTranslationKey() + ".desc";
            this.enchantments.add(new EnchantmentInfo(enchantment, entry.getValue(), I18n.hasTranslation(descriptionKey) ? TextUtil.splitText(Text.literal(I18n.translate(descriptionKey)), 300, MinecraftClient.getInstance().textRenderer) : List.of()));
        }
    }

    @Override
    public int getHeight() {
        return TooltipReforgedConfig.INSTANCE.tooltip.enchantmentTooltip.getValue() ? this.enchantments.stream().reduce(0, (p, c) -> p + c.getHeight(), Integer::sum) : 0;
    }

    @Override
    public int getWidth(TextRenderer textRenderer) {
        return TooltipReforgedConfig.INSTANCE.tooltip.enchantmentTooltip.getValue() ? this.enchantments.stream().reduce(0, (p, c) -> Math.max(p, c.getWidth(textRenderer)), Math::max) : 0;
    }

    @Override
    public void drawItems(TextRenderer textRenderer, int x, int y, DrawContext context) {
        if (!TooltipReforgedConfig.INSTANCE.tooltip.enchantmentTooltip.getValue()) return;
        int currentY = y;
        for (EnchantmentInfo info : this.enchantments) {
            int currentX = x;
            Text name = getEnchantmentName(info.enchantment, info.level);
            context.drawText(textRenderer, name, currentX, currentY, -1, true);
            currentX += textRenderer.getWidth(name) + 2;
            this.drawItem(context, RandomHelper.pick(InfoCollectHelper.getEnchantmentTarget(info.enchantment.target), Items.AIR), currentX, currentY);
            currentX += 12;
            if (MinecraftClient.getInstance().options.advancedItemTooltips)
                context.drawText(textRenderer, info.id.toString(), currentX, currentY, 5592405, true);
            currentY += 10;
            for (MutableText text : info.descriptions) {
                context.drawText(textRenderer, text.formatted(Formatting.DARK_GRAY), x, currentY, -1, true);
                currentY += 10;
            }
        }
    }

    private static Text getEnchantmentName(Enchantment enchantment, int level) {
        MutableText mutableText = Text.translatable(enchantment.getTranslationKey());
        if (enchantment.isCursed()) mutableText.formatted(Formatting.RED);
        else mutableText.formatted(Formatting.GRAY);
        if (level != 1 || enchantment.getMaxLevel() != 1)
            mutableText.append(ScreenTexts.SPACE).append(Text.translatable("enchantment.level." + level)).append(Text.literal("/").append(Text.translatable("enchantment.level." + enchantment.getMaxLevel())).formatted(Formatting.DARK_GRAY));
        return mutableText;
    }

    public void drawItem(DrawContext context, Item item, int x, int y) {
        this.drawItem(context, MinecraftClient.getInstance().player, MinecraftClient.getInstance().world, item, x, y);
    }

    private void drawItem(DrawContext context, @Nullable LivingEntity entity, @Nullable World world, Item item, int x, int y) {
        if (item == Items.AIR) return;
        ItemStack stack = new ItemStack(item);
        MinecraftClient client = MinecraftClient.getInstance();
        BakedModel bakedModel = client.getItemRenderer().getModel(stack, world, entity, 0);
        MatrixStack matrices = context.getMatrices();
        matrices.push();
        matrices.translate(x + 5, y + 4, 150);
        matrices.multiplyPositionMatrix((new Matrix4f()).scaling(1.0F, -1.0F, 1.0F));
        matrices.scale(10, 10, 10);
        boolean bl = !bakedModel.isSideLit();
        if (bl) DiffuseLighting.disableGuiDepthLighting();
        client.getItemRenderer().renderItem(stack, ModelTransformationMode.GUI, false, matrices, context.getVertexConsumers(), 15728880, OverlayTexture.DEFAULT_UV, bakedModel);
        context.draw();
        if (bl) DiffuseLighting.enableGuiDepthLighting();
        matrices.pop();
    }

    private record EnchantmentInfo(Enchantment enchantment, int level, Identifier id, List<MutableText> descriptions) {
        public EnchantmentInfo(Enchantment enchantment, int level, List<MutableText> description) {
            this(enchantment, level, Objects.requireNonNullElse(Registries.ENCHANTMENT.getId(enchantment), Identifier.of("", "")), description);
        }

        public int getWidth(TextRenderer textRenderer) {
            int width = textRenderer.getWidth(getEnchantmentName(this.enchantment, this.level)) + 14 + (MinecraftClient.getInstance().options.advancedItemTooltips ? textRenderer.getWidth(this.id.toString()) : 0);
            for (MutableText text : this.descriptions) width = Math.max(width, textRenderer.getWidth(text));
            return width;
        }

        public int getHeight() {
            return 10 + this.descriptions.size() * 10;
        }
    }
}
