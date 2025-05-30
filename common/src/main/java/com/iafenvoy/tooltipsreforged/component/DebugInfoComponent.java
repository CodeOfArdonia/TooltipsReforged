package com.iafenvoy.tooltipsreforged.component;

import com.google.common.collect.ImmutableList;
import com.iafenvoy.tooltipsreforged.config.TooltipReforgedConfig;
import com.iafenvoy.tooltipsreforged.util.NbtProcessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.InputUtil;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.joml.Matrix4f;

import java.util.List;

public class DebugInfoComponent implements TooltipComponent {
    private static final KeyManager KEY_MANAGER = new KeyManager();
    private final ItemStack stack;
    private final List<String> itemTags, blockTags;
    private final List<Text> nbts;

    public DebugInfoComponent(ItemStack stack) {
        this.stack = stack;
        this.itemTags = this.collectItemTags();
        this.blockTags = this.collectBlockTags();
        this.nbts = NbtProcessor.process(this.stack);
    }

    private List<String> collectItemTags() {
        return Registries.ITEM.getEntry(this.stack.getItem()).streamTags().map(TagKey::id).map(x -> "#" + x.toString()).toList();
    }

    private List<String> collectBlockTags() {
        if (this.stack.getItem() instanceof BlockItem blockItem)
            return Registries.BLOCK.getEntry(blockItem.getBlock()).streamTags().map(TagKey::id).map(x -> "#" + x.toString()).toList();
        else return List.of();
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
        if (!this.nbts.isEmpty()) {
            if (KEY_MANAGER.alt() && !processed) {
                first.append(Text.literal("[ALT NBT] ").formatted(Formatting.GOLD));
                infos = this.nbts.stream().map(Text::copy).toList();
                processed = true;
            } else first.append(Text.literal("[ALT NBT] ").formatted(Formatting.WHITE));
        }
        return ImmutableList.<MutableText>builder().add(first).addAll(infos).build();
    }

    private static class KeyManager {
        private boolean pressing;
        private Pressed pressed;

        public void renderTick() {
            if (InputUtil.isKeyPressed(MinecraftClient.getInstance().getWindow().getHandle(), InputUtil.GLFW_KEY_LEFT_CONTROL)) {
                if (!this.pressing) {
                    this.pressed = this.pressed == Pressed.CTRL ? Pressed.NONE : Pressed.CTRL;
                    this.pressing = true;
                }
            } else if (InputUtil.isKeyPressed(MinecraftClient.getInstance().getWindow().getHandle(), InputUtil.GLFW_KEY_LEFT_SHIFT)) {
                if (!this.pressing) {
                    this.pressed = this.pressed == Pressed.SHIFT ? Pressed.NONE : Pressed.SHIFT;
                    this.pressing = true;
                }
            } else if (InputUtil.isKeyPressed(MinecraftClient.getInstance().getWindow().getHandle(), InputUtil.GLFW_KEY_LEFT_ALT)) {
                if (!this.pressing) {
                    this.pressed = this.pressed == Pressed.ALT ? Pressed.NONE : Pressed.ALT;
                    this.pressing = true;
                }
            } else this.pressing = false;
        }

        public boolean ctrl() {
            return this.pressed == Pressed.CTRL;
        }

        public boolean shift() {
            return this.pressed == Pressed.SHIFT;
        }

        public boolean alt() {
            return this.pressed == Pressed.ALT;
        }

        private enum Pressed {
            NONE, CTRL, SHIFT, ALT
        }
    }
}
