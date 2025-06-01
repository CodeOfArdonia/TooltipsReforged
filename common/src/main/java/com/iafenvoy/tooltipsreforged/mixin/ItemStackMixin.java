package com.iafenvoy.tooltipsreforged.mixin;

import com.iafenvoy.tooltipsreforged.Static;
import com.iafenvoy.tooltipsreforged.config.TooltipReforgedConfig;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Environment(EnvType.CLIENT)
@Mixin({ItemStack.class})
public abstract class ItemStackMixin {
    @Inject(method = "getTooltip", at = @At("RETURN"))
    private void getTooltip(PlayerEntity entity, TooltipContext tooltipContext, CallbackInfoReturnable<List<Text>> info) {
        Static.CACHE = (ItemStack) (Object) this;
    }

    @Inject(method = "appendEnchantments", at = @At("HEAD"), cancellable = true)
    private static void cancelVanillaEnchantmentTooltips(CallbackInfo ci) {
        if (TooltipReforgedConfig.INSTANCE.common.enchantmentTooltip.getValue()) ci.cancel();
    }
}