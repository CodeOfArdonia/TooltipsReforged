package com.iafenvoy.tooltipsreforged.event;

import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.item.ItemStack;

import java.util.List;

public interface TooltipComponentEvent {
    Event<TooltipComponentEvent> EVENT = Event.of(callbacks -> (components, stack) -> callbacks.forEach(x -> x.appendTooltip(components, stack)));

    void appendTooltip(List<TooltipComponent> list, ItemStack stack);
}
