package com.iafenvoy.tooltipsreforged;

import com.iafenvoy.tooltipsreforged.component.*;
import com.iafenvoy.tooltipsreforged.mixin.DecorationItemAccessor;
import com.iafenvoy.tooltipsreforged.util.ExtendedTextVisitor;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.tooltip.OrderedTextTooltipComponent;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityType;
import net.minecraft.item.*;

import java.util.List;

@Environment(EnvType.CLIENT)
public class BuiltinTooltips {
    public static void appendTooltip(ItemStack stack, List<TooltipComponent> components) {
        if (!components.isEmpty()) components.remove(0);
        components.add(0, new HeaderComponent(stack));

        if (stack.getItem() instanceof LingeringPotionItem)
            components.add(1, new PotionEffectsComponent(stack, 0.25f));
        else if (stack.getItem() instanceof PotionItem)
            components.add(1, new PotionEffectsComponent(stack, 1));
        else if (stack.getItem() instanceof TippedArrowItem)
            components.add(1, new PotionEffectsComponent(stack, 0.125f));
        else if (stack.getItem().getFoodComponent() != null)
            components.add(1, new FoodEffectsComponent(stack));
        else if (stack.getItem() instanceof EnchantedBookItem)
            components.add(1, new EnchantmentInfoComponent(EnchantedBookItem.getEnchantmentNbt(stack)));
        else if (stack.getItem().isEnchantable(stack))
            components.add(1, new EnchantmentInfoComponent(EnchantmentHelper.get(stack)));

        if (stack.getItem() instanceof Equipment || stack.getItem() instanceof EntityBucketItem || stack.getItem() instanceof SpawnEggItem)
            components.add(new ModelViewerComponent(stack));
        components.add(new ColorBorderComponent(stack));

        components.add(new ContainerPreviewComponent(stack));
        if (stack.getItem() instanceof FilledMapItem) components.add(new MapComponent(stack));
        if (stack.getItem() instanceof DecorationItemAccessor accessor && accessor.getEntityType() == EntityType.PAINTING)
            components.add(new PaintingComponent(stack));

        if (MinecraftClient.getInstance().options.advancedItemTooltips) {
            for (int i = 0; i < components.size(); i++) {
                TooltipComponent component = components.get(i);
                if (component instanceof OrderedTextTooltipComponent orderedTextTooltipComponent)
                    if (ExtendedTextVisitor.getText(orderedTextTooltipComponent.text).getString().contains((stack.getMaxDamage() - stack.getDamage()) + " / " + stack.getMaxDamage())) {
                        components.set(i, new DurabilityComponent(stack));
                        break;
                    }
            }
            components.add(new DebugInfoComponent(stack));
        } else
            components.add(new DurabilityComponent(stack));
    }
}
