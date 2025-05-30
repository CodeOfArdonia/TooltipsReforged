package com.iafenvoy.tooltipsreforged.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.EntityType;
import net.minecraft.item.DecorationItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Environment(EnvType.CLIENT)
@Mixin(DecorationItem.class)
public interface DecorationItemAccessor {
    @Accessor("entityType")
    EntityType<?> getEntityType();
}
