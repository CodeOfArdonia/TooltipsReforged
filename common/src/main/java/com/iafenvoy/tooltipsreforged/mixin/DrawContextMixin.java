package com.iafenvoy.tooltipsreforged.mixin;

import com.iafenvoy.tooltipsreforged.Static;
import com.iafenvoy.tooltipsreforged.config.TooltipReforgedConfig;
import com.iafenvoy.tooltipsreforged.render.TooltipsRenderHelper;
import com.iafenvoy.tooltipsreforged.util.TooltipScrollTracker;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.client.gui.tooltip.TooltipPositioner;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Environment(EnvType.CLIENT)
@Mixin(DrawContext.class)
public abstract class DrawContextMixin {
    @Unique
    private static final ThreadLocal<ItemStack> PREV_STACK = new ThreadLocal<>();

    @Inject(method = "drawTooltip(Lnet/minecraft/client/font/TextRenderer;Ljava/util/List;IILnet/minecraft/client/gui/tooltip/TooltipPositioner;)V", at = @At("HEAD"), cancellable = true)
    private void injectDrawTooltip(TextRenderer textRenderer, List<TooltipComponent> components, int x, int y, TooltipPositioner positioner, CallbackInfo ci) {
        List<TooltipComponent> mutable = new ArrayList<>(components);
        ItemStack stack = Static.CACHE.get();
        if (stack != null && !TooltipReforgedConfig.INSTANCE.misc.blacklist.getValue().contains(Registries.ITEM.getId(stack.getItem()).toString())) {
            if (PREV_STACK.get() != stack) TooltipScrollTracker.resetScroll();
            else TooltipScrollTracker.tick();
            PREV_STACK.set(stack);
            Static.CACHE.remove();
            TooltipsRenderHelper.render(stack, mutable, (DrawContext) (Object) this, textRenderer, x, y, positioner);
            ci.cancel();
        }
    }
}
