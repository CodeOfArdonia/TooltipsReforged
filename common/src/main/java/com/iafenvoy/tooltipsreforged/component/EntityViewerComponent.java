package com.iafenvoy.tooltipsreforged.component;

import com.iafenvoy.tooltipsreforged.config.TooltipReforgedConfig;
import com.iafenvoy.tooltipsreforged.render.RenderHelper;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.Bucketable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.PufferfishEntity;
import net.minecraft.entity.passive.SnowGolemEntity;
import net.minecraft.item.EntityBucketItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.nbt.NbtCompound;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

@Environment(EnvType.CLIENT)
public class EntityViewerComponent extends StandaloneComponent implements RenderHelper {
    public static final Map<EntityBucketItem, Supplier<? extends EntityType<?>>> ENTITY_BUCKET_MAP = new HashMap<>();
    private static final float ROTATION_INCREMENT = 0.2f;
    private static float CURRENT_ROTATION = 0f;
    private static final int ENTITY_OFFSET = 40;

    public EntityViewerComponent(ItemStack stack) {
        super(stack);
    }

    @Override
    public void render(DrawContext context, TextRenderer textRenderer, int x, int y, int z) {
        CURRENT_ROTATION = (CURRENT_ROTATION + ROTATION_INCREMENT) % 360;
        if (this.stack.getItem() instanceof EntityBucketItem bucketItem) {
            if (TooltipReforgedConfig.INSTANCE.tooltip.bucketTooltip.getValue())
                this.renderBucketEntity(context, x, y, z, bucketItem);
        } else if (this.stack.getItem() instanceof SpawnEggItem spawnEggItem) {
            if (TooltipReforgedConfig.INSTANCE.tooltip.spawnEggTooltip.getValue())
                this.renderSpawnEggEntity(context, x, y, z, spawnEggItem);
        }
    }

    private void renderBucketEntity(DrawContext context, int x, int y, int z, EntityBucketItem bucketItem) {
        if (!ENTITY_BUCKET_MAP.containsKey(bucketItem)) return;
        EntityType<?> entityType = ENTITY_BUCKET_MAP.get(bucketItem).get();
        Entity entity = entityType.create(MinecraftClient.getInstance().world);

        if (entity instanceof Bucketable bucketable && entity instanceof LivingEntity livingEntity) {
            NbtCompound nbtComponent = this.stack.getOrCreateNbt();
            bucketable.copyDataFromNbt(nbtComponent);
            if (entityType == EntityType.TROPICAL_FISH) return;
            if (bucketable instanceof PufferfishEntity pufferfishEntity) pufferfishEntity.setPuffState(2);

            this.renderBackground(context, x - ENTITY_OFFSET - 70, y, 80, 40, z);
            livingEntity.tick();
            this.drawEntity(context, x - 67, y + 40, 30, -CURRENT_ROTATION, livingEntity);
        }
    }

    private void renderSpawnEggEntity(DrawContext context, int x, int y, int z, SpawnEggItem spawnEggItem) {
        EntityType<?> entityType = spawnEggItem.getEntityType(this.stack.getNbt());
        Entity entity = entityType.create(MinecraftClient.getInstance().world);
        if (entity == null) return;

        if (entityType == EntityType.VILLAGER || entityType == EntityType.ZOMBIE_VILLAGER) {
            NbtCompound villagerData = new NbtCompound();
            villagerData.putString("profession", "minecraft:none");
            villagerData.putString("type", "minecraft:plains");
            NbtCompound nbt = new NbtCompound();
            nbt.put("VillagerData", villagerData);
            entity.readNbt(nbt);
        }

        if (entityType == EntityType.TROPICAL_FISH) return;
        if (entity instanceof PufferfishEntity pufferfishEntity) pufferfishEntity.setPuffState(2);
        if (entity instanceof SnowGolemEntity snowGolemEntity) snowGolemEntity.setHasPumpkin(false);

        if (entity instanceof LivingEntity livingEntity) {
            NbtCompound nbt = this.stack.getSubNbt("EntityTag");
            if (nbt != null) livingEntity.readCustomDataFromNbt(nbt);
            this.renderBackground(context, x - ENTITY_OFFSET - 70, y, 80, 80, z);
            livingEntity.tick();
            this.drawEntity(context, x - 67, y + 75, 40, -CURRENT_ROTATION, livingEntity);
        }
    }
}
