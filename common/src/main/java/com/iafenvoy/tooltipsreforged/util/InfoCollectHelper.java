package com.iafenvoy.tooltipsreforged.util;

import com.iafenvoy.tooltipsreforged.TooltipReforgedClient;
import it.unimi.dsi.fastutil.objects.ObjectLongImmutablePair;
import it.unimi.dsi.fastutil.objects.ObjectLongPair;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EntityGroup;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Environment(EnvType.CLIENT)
public final class InfoCollectHelper {
    private static final List<EntityAttribute> ATTRIBUTES = List.of(EntityAttributes.GENERIC_MAX_HEALTH, EntityAttributes.GENERIC_MOVEMENT_SPEED, EntityAttributes.GENERIC_ATTACK_DAMAGE, EntityAttributes.GENERIC_ARMOR, EntityAttributes.GENERIC_FOLLOW_RANGE, EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, EntityAttributes.GENERIC_ATTACK_KNOCKBACK);
    private static final Map<EnchantmentTarget, List<Item>> TARGET_ITEMS_MAP = new HashMap<>();

    public static List<String> collectItemTags(ItemStack stack) {
        return Registries.ITEM.getEntry(stack.getItem()).streamTags().map(TagKey::id).map(x -> "#" + x.toString()).toList();
    }

    public static List<String> collectBlockTags(ItemStack stack) {
        if (stack.getItem() instanceof BlockItem blockItem)
            return Registries.BLOCK.getEntry(blockItem.getBlock()).streamTags().map(TagKey::id).map(x -> "#" + x.toString()).toList();
        else return List.of();
    }

    public static List<String> collectEntityTags(ItemStack stack) {
        if (stack.getItem() instanceof SpawnEggItem eggItem)
            return Registries.ENTITY_TYPE.getEntry(eggItem.getEntityType(stack.getNbt())).streamTags().map(TagKey::id).map(x -> "#" + x.toString()).toList();
        else return List.of();
    }

    public static List<MutableText> collectEntityInfo(ItemStack stack) {
        List<MutableText> list = new LinkedList<>();
        World world = MinecraftClient.getInstance().world;
        if (stack.getItem() instanceof SpawnEggItem eggItem && world != null && eggItem.getEntityType(stack.getNbt()).create(world) instanceof LivingEntity living) {
            DecimalFormat df = new DecimalFormat("#.##");
            ATTRIBUTES.forEach(attribute -> {
                EntityAttributeInstance instance = living.getAttributeInstance(attribute);
                if (instance != null)
                    list.add(Text.translatable(attribute.getTranslationKey()).append(": ").append(df.format(instance.getValue())).formatted(Formatting.GRAY));
            });
            list.add(Text.translatable("text.tooltips_reforged.mob_type").append(Text.translatable(getMobType(living.getGroup()))).formatted(Formatting.GRAY));
        }
        return list;
    }

    public static List<MutableText> collectNbt(ItemStack stack) {
        return NbtProcessor.process(stack);
    }

    private static String getMobType(EntityGroup type) {
        if (type == EntityGroup.DEFAULT) return "type.tooltips_reforged.default";
        if (type == EntityGroup.UNDEAD) return "type.tooltips_reforged.undead";
        if (type == EntityGroup.ARTHROPOD) return "type.tooltips_reforged.arthropod";
        if (type == EntityGroup.ILLAGER) return "type.tooltips_reforged.illager";
        if (type == EntityGroup.AQUATIC) return "type.tooltips_reforged.aquatic";
        return "";
    }

    @Nullable
    public static ObjectLongPair<Identifier> collectLootTable(ItemStack stack) {
        NbtCompound nbt = BlockItem.getBlockEntityNbt(stack);
        if (nbt != null && nbt.contains("LootTable", 8))
            return new ObjectLongImmutablePair<>(Identifier.tryParse(nbt.getString("LootTable")), nbt.getLong("LootTableSeed"));
        else return null;
    }

    public static List<Item> getEnchantmentTarget(EnchantmentTarget target) {
        return TARGET_ITEMS_MAP.getOrDefault(target, List.of());
    }

    @Nullable
    public static DefaultedList<ItemStack> collectContainer(ItemStack stack) {
        DefaultedList<ItemStack> stacks = DefaultedList.ofSize(27, ItemStack.EMPTY);
        NbtCompound nbt = BlockItem.getBlockEntityNbt(stack);
        if (nbt == null || !nbt.contains("Items", NbtElement.LIST_TYPE)) return null;
        Inventories.readNbt(nbt, stacks);
        return stacks;
    }

    static {
        for (EnchantmentTarget target : EnchantmentTarget.values())
            try {
                TARGET_ITEMS_MAP.put(target, Registries.ITEM.stream().filter(target::isAcceptableItem).toList());
            } catch (Throwable e) {
                TooltipReforgedClient.LOGGER.error("Failed to load items for enchantment target {}", target.name(), e);
            }
    }
}
