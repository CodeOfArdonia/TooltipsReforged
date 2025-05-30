package com.iafenvoy.tooltipsreforged.api;

import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.item.ItemStack;

import java.util.List;

public interface TooltipsReforgeEntrypoint {
    void appendTooltip(ItemStack stack, List<TooltipComponent> components);
}
