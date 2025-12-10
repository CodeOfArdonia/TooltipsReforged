package com.iafenvoy.tooltipsreforged.api;

import com.iafenvoy.integration.entrypoint.IntegrationEntryPoint;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.DynamicRegistryManager;

import java.util.List;

public interface TooltipsReforgeEntrypoint extends IntegrationEntryPoint {
    void appendTooltip(ItemStack stack, List<TooltipComponent> components, DynamicRegistryManager registries);
}
