package com.iafenvoy.tooltipsreforged.component;

import com.iafenvoy.tooltipsreforged.Static;
import com.iafenvoy.tooltipsreforged.config.TooltipReforgedConfig;
import com.iafenvoy.tooltipsreforged.render.TooltipProviders;
import com.iafenvoy.tooltipsreforged.render.TooltipsRenderHelper;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

@Environment(EnvType.CLIENT)
public class BackgroundComponent implements TooltipComponent {
    private static final Identifier DEFAULT_BACKGROUND_TEXTURE = Identifier.of(Identifier.DEFAULT_NAMESPACE, "textures/gui/sprites/tooltip/background.png");
    private static final Identifier DEFAULT_FRAME_TEXTURE = Identifier.of(Identifier.DEFAULT_NAMESPACE, "textures/gui/sprites/tooltip/frame.png");
    protected final ItemStack stack;
    private final Identifier texture;

    public BackgroundComponent(ItemStack stack) {
        this.stack = stack;
        this.texture = this.stack.getNbt() != null ? Identifier.tryParse(this.stack.getNbt().getString("tooltip_style")) : null;
    }

    public void render(DrawContext context, int x, int y, int width, int height, int z, int page) throws Exception {
        int i = x - 4;
        int j = y - 4;
        int k = width + 8;
        int l = height + 8;
        context.getMatrices().push();
        this.renderBackground(context, i, j, k, l, z, page);
        this.renderBorder(context, i, j + 1, k, l, z, page);
        context.getMatrices().pop();
    }

    protected void renderBackground(DrawContext context, int x, int y, int width, int height, int z, int page) {
        int bgColor = TooltipReforgedConfig.INSTANCE.misc.backgroundColor.getValue();
        if (TooltipReforgedConfig.INSTANCE.misc.useImageBackground.getValue()) {
            context.getMatrices().translate(0.0F, 0.0F, (float) z);
            TooltipsRenderHelper.drawNineSlicedTexture(context, getBackgroundTexture(this.texture), x - 9, y - 9, width + 18, height + 18, 10, 100, 100);
        } else {
            context.fill(x, y - 1, x + width, y - 1 + 1, z, bgColor);
            context.fill(x, y + height, x + width, y + height + 1, z, bgColor);
            context.fill(x, y, x + width, y + height, z, TooltipReforgedConfig.INSTANCE.misc.backgroundColor.getValue());
            context.fillGradient(x - 1, y, x - 1 + 1, y + height, z, bgColor, bgColor);
            context.fillGradient(x + width, y, x + width + 1, y + height, z, bgColor, bgColor);
        }
    }

    protected void renderBorder(DrawContext context, int x, int y, int width, int height, int z, int page) {
        int stackColor = TooltipProviders.getItemBorderColor(this.stack);
        int startColor = 0xff000000 | stackColor;
        if (stackColor == -1 || stackColor == 0)
            startColor = Objects.requireNonNullElse(Rarity.COMMON.formatting.getColorValue(), -1);
        int endColor = Static.END_COLOR;

        if (TooltipReforgedConfig.INSTANCE.misc.useImageBorder.getValue())
            TooltipsRenderHelper.drawNineSlicedTexture(context, getFrameTexture(this.texture), x - 10, y - 11, width + 20, height + 20, 10, 100, 100);
        else {
            context.fillGradient(x, y, x + 1, y + height - 2, z, startColor, endColor);
            context.fillGradient(x + width - 1, y, x + width - 1 + 1, y + height - 2, z, startColor, endColor);
            context.fill(x, y - 1, x + width, y - 1 + 1, z, startColor);
            context.fill(x, y - 1 + height - 1, x + width, y - 1 + height - 1 + 1, z, endColor);
        }
    }

    @Override
    public int getHeight() {
        return 0;
    }

    @Override
    public int getWidth(TextRenderer textRenderer) {
        return 0;
    }

    protected static Identifier getBackgroundTexture(@Nullable Identifier texture) {
        return texture == null || texture.getPath().isEmpty() ? DEFAULT_BACKGROUND_TEXTURE : texture.withPath((name) -> "textures/gui/sprites/tooltip/" + name + "_background.png");
    }

    protected static Identifier getFrameTexture(@Nullable Identifier texture) {
        return texture == null || texture.getPath().isEmpty() ? DEFAULT_FRAME_TEXTURE : texture.withPath((name) -> "textures/gui/sprites/tooltip/" + name + "_frame.png");
    }
}
