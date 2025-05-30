package com.iafenvoy.tooltipsreforged.mixin;

import com.iafenvoy.tooltipsreforged.component.ModelViewerComponent;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.EntityType;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.EntityBucketItem;
import net.minecraft.item.Item;
import net.minecraft.sound.SoundEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(EntityBucketItem.class)
public class EntityBucketItemMixin {
    @Inject(method = "<init>", at = @At("RETURN"))
    private void saveEntityType(EntityType<?> type, Fluid fluid, SoundEvent emptyingSound, Item.Settings settings, CallbackInfo ci) {
        ModelViewerComponent.ENTITY_BUCKET_MAP.put((EntityBucketItem) (Object) this, () -> type);
    }
}
