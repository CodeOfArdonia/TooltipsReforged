package com.iafenvoy.tooltipsreforged;

import com.iafenvoy.tooltipsreforged.component.*;
import com.iafenvoy.tooltipsreforged.mixin.DecorationItemAccessor;
import com.iafenvoy.tooltipsreforged.util.ExtendedTextVisitor;
import com.iafenvoy.tooltipsreforged.util.TextUtil;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.tooltip.OrderedTextTooltipComponent;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityType;
import net.minecraft.item.*;

import java.util.LinkedList;
import java.util.List;

@Environment(EnvType.CLIENT)
public class BuiltinTooltips {
    public static void appendTooltip(ItemStack stack, List<TooltipComponent> components) {
        if (!components.isEmpty()) components.remove(0);
        List<TooltipComponent> headers = new LinkedList<>();
        //Header
        headers.add(new HeaderComponent(stack));
        headers.add(new ItemZoomComponent(stack));
        //Effects
        if (stack.getItem() instanceof LingeringPotionItem)
            headers.add(new PotionEffectsComponent(stack, 0.25f));
        else if (stack.getItem() instanceof PotionItem)
            headers.add(new PotionEffectsComponent(stack, 1));
        else if (stack.getItem() instanceof TippedArrowItem)
            headers.add(new PotionEffectsComponent(stack, 0.125f));
        //Food
        if (stack.getItem().getFoodComponent() != null)
            headers.add(new FoodEffectComponent(stack));
        //Enchantments
        if (stack.getItem() instanceof EnchantedBookItem)
            headers.add(new EnchantmentsComponent(EnchantedBookItem.getEnchantmentNbt(stack)));
        if (stack.getItem().isEnchantable(stack))
            headers.add(new EnchantmentsComponent(EnchantmentHelper.get(stack)));
        components.addAll(0, headers);

        //Equipments
        if (stack.getItem() instanceof Equipment || stack.getItem() instanceof SkullItem || stack.getItem() instanceof EntityBucketItem || stack.getItem() instanceof SpawnEggItem)
            components.add(new ModelViewerComponent(stack));
        //Misc
        components.add(new ContainerPreviewComponent(stack));
        if (stack.getItem() instanceof FilledMapItem)
            components.add(new MapComponent(stack));
        if (stack.getItem() instanceof DecorationItemAccessor accessor && accessor.getEntityType() == EntityType.PAINTING)
            components.add(new PaintingComponent(stack));
        if (stack.getItem() instanceof BannerPatternItem)
            components.add(new BannerPatternComponent(stack));

        if (MinecraftClient.getInstance().options.advancedItemTooltips) {
            for (int i = 0; i < components.size(); i++) {
                TooltipComponent component = components.get(i);
                if (component instanceof OrderedTextTooltipComponent ordered)
                    if (ExtendedTextVisitor.getText(TextUtil.getTextFromComponent(ordered)).getString().contains((stack.getMaxDamage() - stack.getDamage()) + " / " + stack.getMaxDamage())) {
                        components.set(i, new DurabilityComponent(stack));
                        break;
                    }
            }
            components.add(new DebugInfoComponent(stack));
        } else
            components.add(new DurabilityComponent(stack));
    }
}
