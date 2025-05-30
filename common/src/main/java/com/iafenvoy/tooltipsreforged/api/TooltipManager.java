package com.iafenvoy.tooltipsreforged.api;

import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.item.ItemStack;

import java.util.List;
import java.util.function.Predicate;

public class TooltipManager {
    private final ItemStack stack;
    private final List<TooltipComponent> components;

    public TooltipManager(ItemStack stack, List<TooltipComponent> components) {
        this.stack = stack;
        this.components = components;
    }

    public ItemStack getStack() {
        return this.stack;
    }

    public void append(TooltipComponent component) {
        this.components.add(component);
    }

    public void replace(Predicate<TooltipComponent> predicate, TooltipComponent component) {
        for (int i = 0; i < this.components.size(); i++)
            if (predicate.test(this.components.get(i))) {
                this.set(i, component);
                break;
            }
    }

    public void set(int index, TooltipComponent component) {
        this.components.set(index, component);
    }
}
