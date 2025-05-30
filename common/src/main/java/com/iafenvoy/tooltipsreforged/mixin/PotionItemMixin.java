package com.iafenvoy.tooltipsreforged.mixin;

import com.iafenvoy.tooltipsreforged.config.TooltipReforgedConfig;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.item.PotionItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(PotionItem.class)
public class PotionItemMixin {
    @Inject(method = "appendTooltip", at = @At("HEAD"), cancellable = true)
    private void wrapTooltip(CallbackInfo ci) {
        if (TooltipReforgedConfig.INSTANCE.common.effectsTooltip.getValue()) ci.cancel();
    }
}
