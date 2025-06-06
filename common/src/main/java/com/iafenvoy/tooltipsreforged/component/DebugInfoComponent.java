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

    public DebugInfoComponent(ItemStack stack) {
        this.itemTags = InfoCollectHelper.collectItemTags(stack);
        this.blockTags = InfoCollectHelper.collectBlockTags(stack);
        this.entityTags = InfoCollectHelper.collectEntityTags(stack);
        this.nbt = InfoCollectHelper.collectNbt(stack);
        this.entityInfo = InfoCollectHelper.collectEntityInfo(stack);
        this.lootTable = InfoCollectHelper.collectLootTable(stack);
    }

    @Override
    public int getHeight() {
        return TooltipReforgedConfig.INSTANCE.common.debugInfoTooltip.getValue() ? this.getDisplayTexts().size() * 10 : 0;
    }

    @Override
    public int getWidth(TextRenderer textRenderer) {
        return this.getDisplayTexts().stream().map(textRenderer::getWidth).reduce(0, Math::max, Math::max);
    }

    @Override
    public void drawText(TextRenderer textRenderer, int x, int y, Matrix4f matrix, VertexConsumerProvider.Immediate vertexConsumers) {
        if (!TooltipReforgedConfig.INSTANCE.common.debugInfoTooltip.getValue()) return;
        float currentY = y + 1;
        for (Text text : this.getDisplayTexts()) {
            textRenderer.draw(text, x, currentY, -1, true, matrix, vertexConsumers, TextRenderer.TextLayerType.NORMAL, 0, 0xF000F0);
            currentY += 10;
        }
        KEY_MANAGER.renderTick();
    }

    private List<MutableText> getDisplayTexts() {
        MutableText first = Text.empty();
        List<MutableText> infos = List.of();
        Pair<String, List<MutableText>> ctrlInfo = this.getCtrlInfo();
        if (ctrlInfo != null) {
            if (KEY_MANAGER.ctrl()) {
                first.append(Text.literal("[CTRL %s] ".formatted(I18n.translate(ctrlInfo.getLeft()))).formatted(Formatting.WHITE));
                infos = ctrlInfo.getRight().stream().map(x -> x.formatted(Formatting.DARK_GRAY)).toList();
            } else first.append(Text.literal("[CTRL %s] ".formatted(I18n.translate(ctrlInfo.getLeft()))).formatted(Formatting.GRAY));
        }
        Pair<String, List<MutableText>> shiftInfo = this.getShiftInfo();
        if (shiftInfo != null) {
            if (KEY_MANAGER.shift()) {
                first.append(Text.literal("[SHIFT %s] ".formatted(I18n.translate(shiftInfo.getLeft()))).formatted(Formatting.WHITE));
                infos = shiftInfo.getRight().stream().map(x -> x.formatted(Formatting.DARK_GRAY)).toList();
            } else first.append(Text.literal("[SHIFT %s] ".formatted(I18n.translate(shiftInfo.getLeft()))).formatted(Formatting.GRAY));
        }
        Pair<String, List<MutableText>> altInfo = this.getAltInfo();
        if (altInfo != null) {
            if (KEY_MANAGER.alt()) {
                first.append(Text.literal("[ALT %s] ".formatted(I18n.translate(altInfo.getLeft()))).formatted(Formatting.WHITE));
                infos = altInfo.getRight().stream().map(x -> x.formatted(Formatting.DARK_GRAY)).toList();
            } else first.append(Text.literal("[ALT %s] ".formatted(I18n.translate(altInfo.getLeft()))).formatted(Formatting.GRAY));
        }
        return ImmutableList.<MutableText>builder().add(first).addAll(infos).build();
    }

    @Nullable
    private Pair<String, List<MutableText>> getCtrlInfo() {
        if (!this.itemTags.isEmpty())
            return new Pair<>("tooltip.tooltips_reforged.item_tags", this.itemTags.stream().map(Text::literal).toList());
        return null;
    }

    @Nullable
    private Pair<String, List<MutableText>> getShiftInfo() {
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
    private Pair<String, List<MutableText>> getAltInfo() {
        if (!this.entityInfo.isEmpty())
            return new Pair<>("tooltip.tooltips_reforged.mob_info", this.entityInfo);
        if (!this.nbt.isEmpty())
            return new Pair<>("tooltip.tooltips_reforged.nbt", this.nbt);
        return null;
    }
}
