package com.iafenvoy.tooltipsreforged.forge.mixin;

import com.iafenvoy.tooltipsreforged.component.ModelViewerComponent;
import net.minecraft.entity.EntityType;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.EntityBucketItem;
import net.minecraft.item.Item;
import net.minecraft.sound.SoundEvent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Supplier;

@OnlyIn(Dist.CLIENT)
@Mixin(EntityBucketItem.class)
public class EntityBucketItemMixin {
    @Inject(method = "<init>(Ljava/util/function/Supplier;Ljava/util/function/Supplier;Ljava/util/function/Supplier;Lnet/minecraft/item/Item$Settings;)V", at = @At("RETURN"))
    private void saveEntityType(Supplier<? extends EntityType<?>> entitySupplier, Supplier<? extends Fluid> fluidSupplier, Supplier<? extends SoundEvent> soundSupplier, Item.Settings properties, CallbackInfo ci) {
        ModelViewerComponent.ENTITY_BUCKET_MAP.put((EntityBucketItem) (Object) this, entitySupplier);
    }
}
