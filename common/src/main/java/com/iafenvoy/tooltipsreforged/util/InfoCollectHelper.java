package com.iafenvoy.tooltipsreforged.util;

import net.minecraft.client.MinecraftClient;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityGroup;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;

import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.List;

public final class InfoCollectHelper {
    private static final List<EntityAttribute> ATTRIBUTES = List.of(EntityAttributes.GENERIC_MAX_HEALTH, EntityAttributes.GENERIC_MOVEMENT_SPEED, EntityAttributes.GENERIC_ATTACK_DAMAGE, EntityAttributes.GENERIC_ARMOR, EntityAttributes.GENERIC_FOLLOW_RANGE, EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, EntityAttributes.GENERIC_ATTACK_KNOCKBACK);

    public static List<String> collectItemTags(ItemStack stack) {
        return Registries.ITEM.getEntry(stack.getItem()).streamTags().map(TagKey::id).map(x -> "#" + x.toString()).toList();
    }

    public static List<String> collectBlockTags(ItemStack stack) {
        if (stack.getItem() instanceof BlockItem blockItem)
            return Registries.BLOCK.getEntry(blockItem.getBlock()).streamTags().map(TagKey::id).map(x -> "#" + x.toString()).toList();
        else return List.of();
    }

    public static List<MutableText> collectEntityInfo(ItemStack stack) {
        List<MutableText> list = new LinkedList<>();
        World world = MinecraftClient.getInstance().world;
        if (stack.getItem() instanceof SpawnEggItem eggItem && world != null) {
            Entity entity = eggItem.getEntityType(stack.getNbt()).create(world);
            DecimalFormat df = new DecimalFormat("#.##");
            if (entity instanceof LivingEntity living) {
                ATTRIBUTES.forEach(attribute -> {
                    EntityAttributeInstance instance = living.getAttributeInstance(attribute);
                    if (instance != null)
                        list.add(Text.translatable(attribute.getTranslationKey()).append(":").append(df.format(instance.getValue())).formatted(Formatting.GRAY));
                });
                list.add(Text.translatable("text.tooltips_reforged.mob_type").append(":").append(Text.translatable(getMobType(living.getGroup()))).formatted(Formatting.GRAY));
            }
        }
        return list;
    }

    private static String getMobType(EntityGroup type) {
        if (type == EntityGroup.DEFAULT) return "type.tooltips_reforged.default";
        if (type == EntityGroup.UNDEAD) return "type.tooltips_reforged.undead";
        if (type == EntityGroup.ARTHROPOD) return "type.tooltips_reforged.arthropod";
        if (type == EntityGroup.ILLAGER) return "type.tooltips_reforged.illager";
        if (type == EntityGroup.AQUATIC) return "type.tooltips_reforged.aquatic";
        return "";
    }

    public static List<Item> resolveEnchantmentTarget(EnchantmentTarget target) {
        return switch (target) {
            case ARMOR ->
                    List.of(Items.DIAMOND_HELMET, Items.DIAMOND_CHESTPLATE, Items.DIAMOND_LEGGINGS, Items.DIAMOND_BOOTS);
            case ARMOR_FEET -> List.of(Items.DIAMOND_BOOTS);
            case ARMOR_LEGS -> List.of(Items.DIAMOND_LEGGINGS);
            case ARMOR_CHEST -> List.of(Items.DIAMOND_CHESTPLATE);
            case ARMOR_HEAD -> List.of(Items.DIAMOND_HELMET);
            case WEAPON -> List.of(Items.DIAMOND_SWORD);
            case DIGGER -> List.of(Items.DIAMOND_PICKAXE);
            case FISHING_ROD -> List.of(Items.FISHING_ROD);
            case TRIDENT -> List.of(Items.TRIDENT);
            case BREAKABLE -> List.of(Items.ELYTRA);
            case BOW -> List.of(Items.BOW);
            case WEARABLE -> List.of(Items.LEATHER_CHESTPLATE);
            case CROSSBOW -> List.of(Items.CROSSBOW);
            case VANISHABLE -> List.of(Items.COMPASS);
        };
    }
}
