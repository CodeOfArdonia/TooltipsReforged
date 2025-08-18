package com.iafenvoy.tooltipsreforged.component;

import com.iafenvoy.tooltipsreforged.config.EnchantmentsRenderMode;
import com.iafenvoy.tooltipsreforged.config.TooltipReforgedConfig;
import com.iafenvoy.tooltipsreforged.util.InfoCollectHelper;
import com.iafenvoy.tooltipsreforged.util.RandomHelper;
import com.iafenvoy.tooltipsreforged.util.TextUtil;
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
import net.minecraft.client.util.InputUtil;
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
    private final EnchantmentsRenderMode mode;

    public EnchantmentsComponent(NbtList list) {
        this(EnchantmentHelper.fromNbt(list));
    }

    public EnchantmentsComponent(Map<Enchantment, Integer> map) {
        this.mode = (EnchantmentsRenderMode) TooltipReforgedConfig.INSTANCE.tooltip.enchantmentTooltip.getValue();
        for (Map.Entry<Enchantment, Integer> entry : map.entrySet()) {
            Enchantment enchantment = entry.getKey();
            String descriptionKey = enchantment.getTranslationKey() + ".desc";
            this.enchantments.add(new EnchantmentInfo(enchantment, entry.getValue(), I18n.hasTranslation(descriptionKey) ? TextUtil.splitText(Text.literal(I18n.translate(descriptionKey)), 300, MinecraftClient.getInstance().textRenderer) : List.of()));
        }
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
            Text name = getEnchantmentName(info.enchantment, info.level);
            context.drawText(textRenderer, name, currentX, currentY, -1, true);
            currentX += textRenderer.getWidth(name) + 2;
            this.drawItem(context, RandomHelper.pick(InfoCollectHelper.getEnchantmentTarget(info.enchantment.target), Items.AIR), currentX, currentY);
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

        public int getWidth(TextRenderer textRenderer, boolean includeDetail) {
            int width = textRenderer.getWidth(getEnchantmentName(this.enchantment, this.level)) + 14 + (MinecraftClient.getInstance().options.advancedItemTooltips ? textRenderer.getWidth(this.id.toString()) : 0);
            if (includeDetail)
                for (MutableText text : this.descriptions) width = Math.max(width, textRenderer.getWidth(text));
            return width;
        }

        public int getHeight(boolean includeDetail) {
            return 10 + (includeDetail ? this.descriptions.size() * 10 : 0);
        }
    }
}
