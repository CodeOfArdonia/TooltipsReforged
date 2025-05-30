package com.iafenvoy.tooltipsreforged;

import com.iafenvoy.tooltipsreforged.api.TooltipsProvider;
import com.iafenvoy.tooltipsreforged.api.TooltipsReforgeEntrypoint;
import com.iafenvoy.tooltipsreforged.component.*;
import com.iafenvoy.tooltipsreforged.mixin.DecorationItemAccessor;
import com.iafenvoy.tooltipsreforged.util.ExtendedTextVisitor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.tooltip.OrderedTextTooltipComponent;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.entity.EntityType;
import net.minecraft.item.*;

import java.util.List;

@TooltipsProvider
public class BuiltinTooltips implements TooltipsReforgeEntrypoint {
    @Override
    public void appendTooltip(ItemStack stack, List<TooltipComponent> components) {
        if (!components.isEmpty()) components.remove(0);
        components.add(0, new HeaderTooltipComponent(stack));

        if (stack.getItem() instanceof LingeringPotionItem)
            components.add(1, new PotionEffectsTooltipComponent(stack, 0.25f));
        else if (stack.getItem() instanceof PotionItem)
            components.add(1, new PotionEffectsTooltipComponent(stack, 1));
        else if (stack.getItem() instanceof TippedArrowItem)
            components.add(1, new PotionEffectsTooltipComponent(stack, 0.125f));
        else if (stack.getItem().getFoodComponent() != null)
            components.add(1, new FoodEffectsTooltipComponent(stack));

        if (stack.getItem() instanceof Equipment || stack.getItem() instanceof EntityBucketItem || stack.getItem() instanceof SpawnEggItem)
            components.add(new ModelViewerComponent(stack));
        components.add(new ColorBorderComponent(stack));

        if (stack.getItem() instanceof FilledMapItem) components.add(new MapTooltipComponent(stack));
        if (stack.getItem() instanceof DecorationItemAccessor accessor && accessor.getEntityType() == EntityType.PAINTING)
            components.add(new PaintingTooltipComponent(stack));

        if (MinecraftClient.getInstance().options.advancedItemTooltips) {
            for (int i = 0; i < components.size(); i++) {
                TooltipComponent component = components.get(i);
                if (component instanceof OrderedTextTooltipComponent orderedTextTooltipComponent)
                    if (ExtendedTextVisitor.get(orderedTextTooltipComponent.text).getString().contains((stack.getMaxDamage() - stack.getDamage()) + " / " + stack.getMaxDamage())) {
                        components.set(i, new DurabilityTooltipComponent(stack));
                        break;
                    }
            }
            components.add(new DebugInfoComponent(stack));
        } else
            components.add(new DurabilityTooltipComponent(stack));
    }
}
