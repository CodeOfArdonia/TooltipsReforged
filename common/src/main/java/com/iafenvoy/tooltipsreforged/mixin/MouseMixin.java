package com.iafenvoy.tooltipsreforged.mixin;

import com.iafenvoy.tooltipsreforged.config.TooltipReforgedConfig;
import com.iafenvoy.tooltipsreforged.util.TooltipScrollTracker;
import net.minecraft.client.Mouse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Mouse.class)
public class MouseMixin {
    @Inject(method = "onMouseScroll(JDD)V", at = @At("HEAD"))
    private void trackWheel(long window, double horizontal, double vertical, CallbackInfo info) {
        if (!TooltipReforgedConfig.INSTANCE.common.mouseScrollTooltip.getValue()) return;
        if (vertical > 0) TooltipScrollTracker.scrollDown();
        if (vertical < 0) TooltipScrollTracker.scrollUp();
    }
}
