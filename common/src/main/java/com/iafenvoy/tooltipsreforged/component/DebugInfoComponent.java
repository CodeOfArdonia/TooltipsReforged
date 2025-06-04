package com.iafenvoy.tooltipsreforged.component;

import com.google.common.collect.ImmutableList;
import com.iafenvoy.tooltipsreforged.config.TooltipReforgedConfig;
import com.iafenvoy.tooltipsreforged.util.InfoCollectHelper;
import com.iafenvoy.tooltipsreforged.util.TooltipKeyManager;
import it.unimi.dsi.fastutil.objects.ObjectLongPair;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import org.joml.Matrix4f;

import java.util.List;

public class DebugInfoComponent implements TooltipComponent {
    private static final TooltipKeyManager KEY_MANAGER = new TooltipKeyManager();
    private final List<String> itemTags, blockTags;
    private final List<MutableText> nbt, entityInfo;
    private final ObjectLongPair<Identifier> lootTable;

    public DebugInfoComponent(ItemStack stack) {
        this.itemTags = InfoCollectHelper.collectItemTags(stack);
        this.blockTags = InfoCollectHelper.collectBlockTags(stack);
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
        boolean processed = false;
        if (!this.itemTags.isEmpty()) {
            if (KEY_MANAGER.ctrl()) {
                first.append(Text.literal("[CTRL Item Tags] ").formatted(Formatting.GOLD));
                infos = this.itemTags.stream().map(Text::literal).map(x -> x.formatted(Formatting.GRAY)).toList();
                processed = true;
            } else first.append(Text.literal("[CTRL Item Tags] ").formatted(Formatting.WHITE));
        }
        if (!this.nbt.isEmpty()) {
            if (KEY_MANAGER.alt() && !processed) {
                first.append(Text.literal("[ALT NBT] ").formatted(Formatting.GOLD));
                infos = this.nbt.stream().map(Text::copy).toList();
                processed = true;
            } else first.append(Text.literal("[ALT NBT] ").formatted(Formatting.WHITE));
        }
        Pair<String, List<MutableText>> specific = this.getSpecificInfo();
        if (specific != null) {
            if (KEY_MANAGER.shift() && !processed) {
                first.append(Text.literal("[SHIFT %s] ".formatted(specific.getLeft())).formatted(Formatting.GOLD));
                infos = specific.getRight().stream().map(x -> x.formatted(Formatting.GRAY)).toList();
            } else first.append(Text.literal("[SHIFT %s] ".formatted(specific.getLeft())).formatted(Formatting.WHITE));
        }
        return ImmutableList.<MutableText>builder().add(first).addAll(infos).build();
    }

    private Pair<String, List<MutableText>> getSpecificInfo() {
        if (!this.blockTags.isEmpty())
            return new Pair<>("Block Tags", this.blockTags.stream().map(Text::literal).toList());
        if (!this.entityInfo.isEmpty())
            return new Pair<>("Mob Info", this.entityInfo);
        if (this.lootTable != null && this.lootTable.left() != null)
            return new Pair<>("Loot Table", List.of(
                    Text.literal("Id: " + this.lootTable.left().toString()),
                    Text.literal("Seed: " + this.lootTable.rightLong())
            ));
        return null;
    }
}
