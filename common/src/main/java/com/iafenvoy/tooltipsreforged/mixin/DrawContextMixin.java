package com.iafenvoy.tooltipsreforged.mixin;

import com.iafenvoy.tooltipsreforged.BuiltinTooltips;
import com.iafenvoy.tooltipsreforged.EntryPointLoader;
import com.iafenvoy.tooltipsreforged.Static;
import com.iafenvoy.tooltipsreforged.render.TooltipsRenderHelper;
import com.iafenvoy.tooltipsreforged.util.TooltipScrollTracker;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.tooltip.HoveredTooltipPositioner;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.client.gui.tooltip.TooltipPositioner;
import net.minecraft.item.ItemStack;
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
    private static ItemStack tooltips_reforged$prevStack;

    @Inject(method = "drawTooltip(Lnet/minecraft/client/font/TextRenderer;Ljava/util/List;IILnet/minecraft/client/gui/tooltip/TooltipPositioner;)V", at = @At("HEAD"), cancellable = true)
    private void injectDrawTooltip(TextRenderer textRenderer, List<TooltipComponent> components, int x, int y, TooltipPositioner positioner, CallbackInfo ci) {
        List<TooltipComponent> mutable = new ArrayList<>(components);
        if (Static.CACHE != null && EntryPointLoader.INSTANCE != null) {
            if (tooltips_reforged$prevStack != Static.CACHE) TooltipScrollTracker.resetScroll();
            else TooltipScrollTracker.tick();
            BuiltinTooltips.appendTooltip(Static.CACHE, mutable);
            EntryPointLoader.INSTANCE.getEntries().forEach(e -> e.appendTooltip(Static.CACHE, mutable));
            TooltipsRenderHelper.drawTooltip((DrawContext) (Object) this, textRenderer, mutable, x + TooltipScrollTracker.getXOffset(), y + TooltipScrollTracker.getYOffset(), HoveredTooltipPositioner.INSTANCE);
            tooltips_reforged$prevStack = Static.CACHE;
            Static.CACHE = null;
            ci.cancel();
        }
    }
}
