package com.iafenvoy.tooltipsreforged;

import com.iafenvoy.tooltipsreforged.component.*;
import com.iafenvoy.tooltipsreforged.component.MapTooltipComponent;
import com.iafenvoy.tooltipsreforged.component.PaintingTooltipComponent;
import com.iafenvoy.tooltipsreforged.mixin.DecorationItemAccessor;
import com.iafenvoy.tooltipsreforged.util.ExtendedTextVisitor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.tooltip.OrderedTextTooltipComponent;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.entity.EntityType;
import net.minecraft.item.*;

import java.util.List;

public class TooltipManager {
    public static void appendTooltip(List<TooltipComponent> list, ItemStack stack) {
        list.set(0, new HeaderTooltipComponent(stack));

        if (stack.getItem() instanceof LingeringPotionItem)
            list.add(1, new PotionEffectsTooltipComponent(stack, 0.25f));
        else if (stack.getItem() instanceof PotionItem)
            list.add(1, new PotionEffectsTooltipComponent(stack, 1));
        else if (stack.getItem() instanceof TippedArrowItem)
            list.add(1, new PotionEffectsTooltipComponent(stack, 0.125f));
        else if (stack.getItem().getFoodComponent() != null)
            list.add(1, new FoodEffectsTooltipComponent(stack));

        if (stack.getItem() instanceof Equipment || stack.getItem() instanceof EntityBucketItem || stack.getItem() instanceof SpawnEggItem)
            list.add(new ModelViewerComponent(stack));
        list.add(new ColorBorderComponent(stack));

        if (stack.getItem() instanceof FilledMapItem) list.add(new MapTooltipComponent(stack));
        if (stack.getItem() instanceof DecorationItemAccessor accessor && accessor.getEntityType() == EntityType.PAINTING)
            list.add(new PaintingTooltipComponent(stack));

        if (MinecraftClient.getInstance().options.advancedItemTooltips) {
            for (TooltipComponent component : list)
                if (component instanceof OrderedTextTooltipComponent orderedTextTooltipComponent)
                    if (ExtendedTextVisitor.get(orderedTextTooltipComponent.text).getString().contains((stack.getMaxDamage() - stack.getDamage()) + " / " + stack.getMaxDamage())) {
                        list.remove(component);
                        break;
                    }
            list.add(list.size() - 3, new DurabilityTooltipComponent(stack));
        } else
            list.add(new DurabilityTooltipComponent(stack));
    }
}
