package com.iafenvoy.tooltipsreforged.component;

import com.google.common.collect.ImmutableList;
import com.iafenvoy.tooltipsreforged.config.TooltipReforgedConfig;
import com.iafenvoy.tooltipsreforged.util.InfoCollectHelper;
import com.iafenvoy.tooltipsreforged.util.TooltipKeyManager;
import it.unimi.dsi.fastutil.objects.ObjectLongPair;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;

import java.util.List;

@Environment(EnvType.CLIENT)
public class DebugInfoComponent implements TooltipComponent {
    private static final TooltipKeyManager KEY_MANAGER = new TooltipKeyManager();
    private final List<String> itemTags, blockTags, entityTags;
    private final List<MutableText> nbt, entityInfo;
    private final ObjectLongPair<Identifier> lootTable;

    public DebugInfoComponent(ItemStack stack, DynamicRegistryManager registries) {
        this.itemTags = InfoCollectHelper.collectItemTags(stack);
        this.blockTags = InfoCollectHelper.collectBlockTags(stack);
        this.entityTags = InfoCollectHelper.collectEntityTags(stack);
        this.nbt = InfoCollectHelper.collectNbt(stack, registries);
        this.entityInfo = InfoCollectHelper.collectEntityInfo(stack);
        this.lootTable = InfoCollectHelper.collectLootTable(stack);
    }

    @Override
    public int getHeight() {
        return TooltipReforgedConfig.INSTANCE.tooltip.debugInfoTooltip.getValue() ? this.getDisplayTexts().size() * 10 : 0;
    }

    @Override
    public int getWidth(TextRenderer textRenderer) {
        return this.getDisplayTexts().stream().map(textRenderer::getWidth).reduce(0, Math::max, Math::max);
    }

    @Override
    public void drawText(TextRenderer textRenderer, int x, int y, Matrix4f matrix, VertexConsumerProvider.Immediate vertexConsumers) {
        if (!TooltipReforgedConfig.INSTANCE.tooltip.debugInfoTooltip.getValue()) return;
        float currentY = y + 1;
        for (Text text : this.getDisplayTexts()) {
            textRenderer.draw(text, x, currentY, -1, true, matrix, vertexConsumers, TextRenderer.TextLayerType.NORMAL, 0, 0xF000F0);
            currentY += 10;
        }
        KEY_MANAGER.renderTick();
    }

    private List<MutableText> getDisplayTexts() {
        MutableText first = Text.empty();
        boolean hasInfo = false;
        List<MutableText> infos = List.of();
        Pair<String, List<MutableText>> itemTagInfo = this.getItemTagInfo();
        TooltipKeyManager.PressState itemTagState = KEY_MANAGER.itemTag();
        if (itemTagInfo != null && itemTagState.show()) {
            hasInfo = true;
            if (itemTagState.showDetail()) {
                first.append(Text.literal("[%s %s] ".formatted(TooltipKeyManager.itemTagKeyTranslation(), I18n.translate(itemTagInfo.getLeft()))).formatted(Formatting.WHITE));
                infos = itemTagInfo.getRight().stream().map(x -> x.formatted(Formatting.DARK_GRAY)).toList();
            } else
                first.append(Text.literal("[%s %s] ".formatted(TooltipKeyManager.itemTagKeyTranslation(), I18n.translate(itemTagInfo.getLeft()))).formatted(Formatting.GRAY));
        }
        Pair<String, List<MutableText>> specificInfo = this.getSpecificInfo();
        TooltipKeyManager.PressState specificState = KEY_MANAGER.specific();
        if (specificInfo != null && specificState.show()) {
            hasInfo = true;
            if (specificState.showDetail()) {
                first.append(Text.literal("[%s %s] ".formatted(TooltipKeyManager.specificKeyTranslation(), I18n.translate(specificInfo.getLeft()))).formatted(Formatting.WHITE));
                infos = specificInfo.getRight().stream().map(x -> x.formatted(Formatting.DARK_GRAY)).toList();
            } else
                first.append(Text.literal("[%s %s] ".formatted(TooltipKeyManager.specificKeyTranslation(), I18n.translate(specificInfo.getLeft()))).formatted(Formatting.GRAY));
        }
        Pair<String, List<MutableText>> nbtInfo = this.getNbtInfo();
        TooltipKeyManager.PressState nbtState = KEY_MANAGER.nbt();
        if (nbtInfo != null && nbtState.show()) {
            hasInfo = true;
            if (nbtState.showDetail()) {
                first.append(Text.literal("[%s %s] ".formatted(TooltipKeyManager.nbtKeyTranslation(), I18n.translate(nbtInfo.getLeft()))).formatted(Formatting.WHITE));
                infos = nbtInfo.getRight().stream().map(x -> x.formatted(Formatting.DARK_GRAY)).toList();
            } else
                first.append(Text.literal("[%s %s] ".formatted(TooltipKeyManager.nbtKeyTranslation(), I18n.translate(nbtInfo.getLeft()))).formatted(Formatting.GRAY));
        }
        return hasInfo ? ImmutableList.<MutableText>builder().add(first).addAll(infos).build() : List.of();
    }

    @Nullable
    private Pair<String, List<MutableText>> getItemTagInfo() {
        if (!this.itemTags.isEmpty())
            return new Pair<>("tooltip.tooltips_reforged.item_tags", this.itemTags.stream().map(Text::literal).toList());
        return null;
    }

    @Nullable
    private Pair<String, List<MutableText>> getSpecificInfo() {
        if (!this.blockTags.isEmpty())
            return new Pair<>("tooltip.tooltips_reforged.block_tags", this.blockTags.stream().map(Text::literal).toList());
        if (!this.entityTags.isEmpty())
            return new Pair<>("tooltip.tooltips_reforged.entity_tags", this.entityTags.stream().map(Text::literal).toList());
        if (this.lootTable != null && this.lootTable.left() != null)
            return new Pair<>("tooltip.tooltips_reforged.loot_table", List.of(
                    Text.literal("tooltip.tooltips_reforged.id" + this.lootTable.left().toString()),
                    Text.literal("tooltip.tooltips_reforged.seed" + this.lootTable.rightLong())
            ));
        return null;
    }

    @Nullable
    private Pair<String, List<MutableText>> getNbtInfo() {
        if (!this.entityInfo.isEmpty())
            return new Pair<>("tooltip.tooltips_reforged.mob_info", this.entityInfo);
        if (!this.nbt.isEmpty())
            return new Pair<>("tooltip.tooltips_reforged.nbt", this.nbt);
        return null;
    }
}
