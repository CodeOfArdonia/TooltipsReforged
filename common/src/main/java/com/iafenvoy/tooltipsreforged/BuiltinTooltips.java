package com.iafenvoy.tooltipsreforged;

import com.iafenvoy.tooltipsreforged.component.*;
import com.iafenvoy.tooltipsreforged.mixin.DecorationItemAccessor;
import com.iafenvoy.tooltipsreforged.util.TextUtil;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.SkullBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.tooltip.OrderedTextTooltipComponent;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.entity.EntityType;
import net.minecraft.item.*;
import net.minecraft.text.OrderedText;
import net.minecraft.registry.DynamicRegistryManager;

import java.util.LinkedList;
import java.util.List;

@Environment(EnvType.CLIENT)
public class BuiltinTooltips {
    public static void appendTooltip(ItemStack stack, List<TooltipComponent> components, DynamicRegistryManager registries) {
        OrderedText text = null;
        if (!components.isEmpty()) {
            TooltipComponent component = components.remove(0);
            if (component instanceof OrderedTextTooltipComponent ordered)
                text = TextUtil.getTextFromComponent(ordered);
        }
        List<TooltipComponent> headers = new LinkedList<>();
        //Header
        headers.add(new HeaderComponent(stack, text));
        headers.add(new ItemZoomComponent(stack));
        //Effects
        if (stack.getItem() instanceof LingeringPotionItem)
            headers.add(new PotionEffectsComponent(stack, 0.25f));
        else if (stack.getItem() instanceof PotionItem)
            headers.add(new PotionEffectsComponent(stack, 1));
        else if (stack.getItem() instanceof TippedArrowItem)
            headers.add(new PotionEffectsComponent(stack, 0.125f));
        //Food
        if (stack.contains(DataComponentTypes.FOOD))
            headers.add(new FoodEffectComponent(stack));
        //Enchantments
        if (stack.getItem() instanceof EnchantedBookItem)
            headers.add(new EnchantmentsComponent(stack.getOrDefault(DataComponentTypes.STORED_ENCHANTMENTS, ItemEnchantmentsComponent.DEFAULT)));
        if (stack.getItem().isEnchantable(stack))
            headers.add(new EnchantmentsComponent(stack.getEnchantments()));
        components.addAll(0, headers);
        //Equipments
        if (stack.getItem() instanceof Equipment || stack.getItem() instanceof BlockItem blockItem && blockItem.getBlock() instanceof SkullBlock) {
            components.add(new EquipmentViewerComponent(stack));
            components.add(new EquipmentCompareComponent(stack));
        }
        //Misc
        if (stack.getItem() instanceof EntityBucketItem || stack.getItem() instanceof SpawnEggItem)
            components.add(new EntityViewerComponent(stack));
        components.add(new ContainerPreviewComponent(stack, registries));
        if (stack.getItem() instanceof FilledMapItem)
            components.add(new MapComponent(stack));
        if (stack.getItem() instanceof DecorationItemAccessor accessor && accessor.getEntityType() == EntityType.PAINTING)
            components.add(new PaintingComponent(stack));
        if (stack.getItem() instanceof BannerPatternItem)
            components.add(new BannerPatternComponent(stack, registries));
        //Debug & Durability
        components.add(new DurabilityComponent(stack));
        if (MinecraftClient.getInstance().options.advancedItemTooltips)
            components.add(new DebugInfoComponent(stack, registries));
    }
}
