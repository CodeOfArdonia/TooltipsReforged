package com.iafenvoy.tooltipsreforged.render;

import com.iafenvoy.tooltipsreforged.Static;
import com.iafenvoy.tooltipsreforged.config.TooltipReforgedConfig;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class ExtendedTooltipBackgroundRenderer {
    private static final Identifier DEFAULT_BACKGROUND_TEXTURE = Identifier.of(Identifier.DEFAULT_NAMESPACE, "textures/gui/sprites/tooltip/background.png");
    private static final Identifier DEFAULT_FRAME_TEXTURE = Identifier.of(Identifier.DEFAULT_NAMESPACE, "textures/gui/sprites/tooltip/frame.png");

    public static void render(ItemStack stack, DrawContext context, int x, int y, int width, int height, int z) {
        NbtComponent component = stack.get(DataComponentTypes.CUSTOM_DATA);
        Identifier texture = component != null ? Identifier.tryParse(component.copyNbt().getString("tooltip_style")) : null;
        int i = x - 4;
        int j = y - 4;
        int k = width + 8;
        int l = height + 8;

        context.getMatrices().push();
        //Background
        int bgColor = TooltipReforgedConfig.INSTANCE.misc.backgroundColor.getValue();
        if (TooltipReforgedConfig.INSTANCE.misc.useImageBackground.getValue()) {
            context.getMatrices().translate(0, 0, z);
            TooltipsRenderHelper.drawNineSlicedTexture(context, getBackgroundTexture(texture), i - 9, j - 9, k + 18, l + 18, 10, 100, 100);
        } else {
            context.fill(i, j - 1, i + k, j, z, bgColor);
            context.fill(i, j + l, i + k, j + l + 1, z, bgColor);
            context.fill(i, j, i + k, j + l, z, TooltipReforgedConfig.INSTANCE.misc.backgroundColor.getValue());
            context.fillGradient(i - 1, j, i, j + l, z, bgColor, bgColor);
            context.fillGradient(i + k, j, i + k + 1, j + l, z, bgColor, bgColor);
        }
        //Border
        int stackColor = TooltipProviders.getItemBorderColor(stack);
        int startColor = 0xff000000 | stackColor;
        if (stackColor == -1 || stackColor == 0)
            startColor = Objects.requireNonNullElse(Rarity.COMMON.getFormatting().getColorValue(), -1);
        int endColor = Static.END_COLOR;

        if (TooltipReforgedConfig.INSTANCE.misc.useImageBorder.getValue())
            TooltipsRenderHelper.drawNineSlicedTexture(context, getFrameTexture(texture), i - 10, j - 10, k + 20, l + 20, 10, 100, 100);
        else {
            context.fillGradient(i, j + 1, i + 1, j + l - 1, z, startColor, endColor);
            context.fillGradient(i + k - 1, j + 1, i + k, j + l - 1, z, startColor, endColor);
            context.fill(i, j, i + k, j + 1, z, startColor);
            context.fill(i, j + l - 1, i + k, j + l, z, endColor);
        }
        context.getMatrices().pop();
    }

    protected static Identifier getBackgroundTexture(@Nullable Identifier texture) {
        return texture == null || texture.getPath().isEmpty() ? DEFAULT_BACKGROUND_TEXTURE : texture.withPath((name) -> "textures/gui/sprites/tooltip/" + name + "_background.png");
    }

    protected static Identifier getFrameTexture(@Nullable Identifier texture) {
        return texture == null || texture.getPath().isEmpty() ? DEFAULT_FRAME_TEXTURE : texture.withPath((name) -> "textures/gui/sprites/tooltip/" + name + "_frame.png");
    }
}
