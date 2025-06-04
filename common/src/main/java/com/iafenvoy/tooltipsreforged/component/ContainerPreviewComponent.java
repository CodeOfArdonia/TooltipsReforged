package com.iafenvoy.tooltipsreforged.component;

import com.iafenvoy.tooltipsreforged.TooltipReforgedClient;
import com.iafenvoy.tooltipsreforged.config.TooltipReforgedConfig;
import com.iafenvoy.tooltipsreforged.util.InfoCollectHelper;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import org.jetbrains.annotations.Nullable;

public class ContainerPreviewComponent implements TooltipComponent {
    private static final Identifier BACKGROUND = Identifier.of(TooltipReforgedClient.MOD_ID, "textures/gui/container_preview.png");
    @Nullable
    private final DefaultedList<ItemStack> stacks;

    public ContainerPreviewComponent(ItemStack stack) {
        this.stacks = InfoCollectHelper.collectContainer(stack);
    }

    @Override
    public int getHeight() {
        return this.stacks == null || !TooltipReforgedConfig.INSTANCE.common.containerTooltip.getValue() ? 0 : 69;
    }

    @Override
    public int getWidth(TextRenderer textRenderer) {
        return this.stacks == null || !TooltipReforgedConfig.INSTANCE.common.containerTooltip.getValue() ? 0 : 175;
    }

    @Override
    public void drawItems(TextRenderer textRenderer, int x, int y, DrawContext context) {
        if (this.stacks == null || !TooltipReforgedConfig.INSTANCE.common.containerTooltip.getValue()) return;
        context.drawTexture(BACKGROUND, x, y, 0, 0, 256, 256);
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 9; j++) {
                int index = i * 9 + j;
                if (this.stacks.size() > index) {
                    ItemStack stack = this.stacks.get(index);
                    context.drawItem(stack, x + 8 + j * 18, y + 8 + i * 18);
                    context.drawItemInSlot(textRenderer, stack, x + 8 + j * 18, y + 8 + i * 18);
                }
            }
    }
}
