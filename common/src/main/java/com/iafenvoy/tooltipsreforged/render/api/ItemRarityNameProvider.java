package com.iafenvoy.tooltipsreforged.render.api;

import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

public interface ItemRarityNameProvider {
    Text getRarityName(ItemStack stack);
}
