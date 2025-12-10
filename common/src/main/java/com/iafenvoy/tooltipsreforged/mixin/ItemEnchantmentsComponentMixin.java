package com.iafenvoy.tooltipsreforged.mixin;

import com.iafenvoy.tooltipsreforged.config.TooltipReforgedConfig;
import com.iafenvoy.tooltipsreforged.config.mode.EnchantmentsRenderMode;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemEnchantmentsComponent.class)
public class ItemEnchantmentsComponentMixin {
    @Inject(method = "appendTooltip", at = @At("HEAD"), cancellable = true)
    private static void cancelVanillaEnchantmentTooltips(CallbackInfo ci) {
        if (((EnchantmentsRenderMode) TooltipReforgedConfig.INSTANCE.tooltip.enchantmentTooltip.getValue()).shouldRender())
            ci.cancel();
    }
}
