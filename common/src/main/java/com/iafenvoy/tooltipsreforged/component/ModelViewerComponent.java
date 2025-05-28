package com.iafenvoy.tooltipsreforged.component;

import com.iafenvoy.tooltipsreforged.config.TooltipReforgedConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.DiffuseLighting;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.entity.*;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.mob.GhastEntity;
import net.minecraft.entity.passive.*;
import net.minecraft.item.EntityBucketItem;
import net.minecraft.item.Equipment;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.nbt.NbtCompound;
import org.joml.Matrix4f;
import org.joml.Quaternionf;

public class ModelViewerComponent extends ColorBorderComponent {
    private static final float ROTATION_INCREMENT = 0.2f;
    private static float CURRENT_ROTATION = 0f;

    private static final int ENTITY_SIZE = 30;
    private static final int SPACING = 20;
    private static final int ENTITY_OFFSET = ENTITY_SIZE + SPACING - 10;
    private static final int SHADOW_LIGHT_COLOR = 15728880;

    private final ItemStack stack;
    private final TooltipReforgedConfig config;

    public ModelViewerComponent(ItemStack stack) {
        super(stack);
        this.stack = stack;
        this.config = TooltipReforgedConfig.INSTANCE;
    }

    @Override
    public void render(DrawContext context, int x, int y, int width, int height, int z, int page) throws Exception {
        super.render(context, x, y, width, height, z, page);
        if (page != 0) return;
        CURRENT_ROTATION = (CURRENT_ROTATION + ROTATION_INCREMENT) % 360;

        if (this.stack.getItem() instanceof Equipment) {
            if (!this.config.common.armorTooltip.getValue()) return;
            if (this.config.common.usePlayer.getValue()) this.renderPlayer(context, x, y, z);
            else this.renderArmorStand(context, x, y, z);
        } else if (this.stack.getItem() instanceof EntityBucketItem bucketItem) {
            if (!this.config.common.bucketTooltip.getValue()) return;
            this.renderBucketEntity(context, x, y, z, bucketItem);
        } else if (this.stack.getItem() instanceof SpawnEggItem spawnEggItem) {
            if (!this.config.common.spawnEggTooltip.getValue()) return;
            this.renderSpawnEggEntity(context, x, y, z, spawnEggItem);
        }
    }

    private void renderPlayer(DrawContext context, int x, int y, int z) throws Exception {
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        if (player == null) return;
        EquipmentSlot slot = LivingEntity.getPreferredEquipmentSlot(this.stack);
        ItemStack original = player.getEquippedStack(slot);
        player.equipStack(slot, this.stack);
        super.render(context, x - ENTITY_OFFSET - 25, y, ENTITY_SIZE + 10, ENTITY_SIZE + 30 + 10, z, -1);
        drawEntity(context, x - ENTITY_SIZE / 2 - SPACING - 10, y + ENTITY_SIZE + 30 + 5, ENTITY_SIZE, CURRENT_ROTATION, player);
        player.equipStack(slot, original);
    }

    private void renderArmorStand(DrawContext context, int x, int y, int z) throws Exception {
        ArmorStandEntity armorStand = new ArmorStandEntity(EntityType.ARMOR_STAND, MinecraftClient.getInstance().world);
        armorStand.equipStack(LivingEntity.getPreferredEquipmentSlot(this.stack), this.stack);
        super.render(context, x - ENTITY_OFFSET - 25, y, ENTITY_SIZE + 10, ENTITY_SIZE + 30 + 10, z, -1);
        drawEntity(context, x - ENTITY_SIZE / 2 - SPACING - 10, y + ENTITY_SIZE + 30 + 5, ENTITY_SIZE, CURRENT_ROTATION, armorStand);
    }

    private void renderBucketEntity(DrawContext context, int x, int y, int z, EntityBucketItem bucketItem) throws Exception {
        EntityType<?> entityType = bucketItem.entityType;
        Entity entity = entityType.create(MinecraftClient.getInstance().world);

        if (entity instanceof Bucketable bucketable) {
            var nbtComponent = this.stack.getOrCreateNbt();
            bucketable.copyDataFromNbt(nbtComponent);
            if (entityType == EntityType.TROPICAL_FISH) return;
            if (bucketable instanceof PufferfishEntity pufferfishEntity) pufferfishEntity.setPuffState(2);

            super.render(context, x - ENTITY_OFFSET - 70, y, ENTITY_SIZE + 50, ENTITY_SIZE + 10, z, -1);
            drawEntity(context, x - ENTITY_SIZE / 2 - SPACING - 35, y + ENTITY_SIZE, ENTITY_SIZE, CURRENT_ROTATION, (LivingEntity) bucketable);
        }
    }

    private void renderSpawnEggEntity(DrawContext context, int x, int y, int z, SpawnEggItem spawnEggItem) throws Exception {
        var entityType = spawnEggItem.getEntityType(null);
        var entity = entityType.create(MinecraftClient.getInstance().world);
        if (entity == null) return;

        if (entityType == EntityType.VILLAGER || entityType == EntityType.ZOMBIE_VILLAGER) {
            var villagerData = new NbtCompound();
            villagerData.putString("profession", "minecraft:none");
            villagerData.putString("type", "minecraft:plains");
            var nbt = this.stack.getOrCreateNbt();

            nbt.put("VillagerData", villagerData);
            entity.readNbt(nbt);
        }

        if (entityType == EntityType.TROPICAL_FISH) return;
        if (entity instanceof PufferfishEntity pufferfishEntity) pufferfishEntity.setPuffState(2);
        if (entity instanceof SnowGolemEntity snowGolemEntity) snowGolemEntity.setHasPumpkin(false);

        if (entity instanceof LivingEntity livingEntity) {
            super.render(context, x - ENTITY_OFFSET - 70, y, ENTITY_SIZE + 50, ENTITY_SIZE + 50, z, -1);

            int size = ENTITY_SIZE;
            if (entity instanceof GhastEntity) size = 10;
            if (entity instanceof CamelEntity) size = 20;

            drawEntity(context, x - size / 2 - SPACING - 35, y + size * 2 + SPACING, size, CURRENT_ROTATION, livingEntity);
        }
    }

    public static void drawEntity(DrawContext context, int x, int y, int size, float rotationYaw, LivingEntity entity) {
        float pitch = entity.getPitch(), yaw = entity.getYaw(), bodyYaw = entity.bodyYaw, headYaw = entity.headYaw;
        entity.setPitch(0);
        entity.setYaw(rotationYaw);
        entity.bodyYaw = rotationYaw;
        entity.headYaw = rotationYaw;

        Quaternionf correctionRotation = entity instanceof CodEntity || entity instanceof SalmonEntity ? new Quaternionf().rotateZ((float) Math.toRadians(-90)) : new Quaternionf().rotateX((float) Math.toRadians(180));
        Quaternionf combinedRotation = new Quaternionf().rotateY((float) Math.toRadians(rotationYaw)).mul(correctionRotation);
        drawEntity(context, x, y, size, combinedRotation, entity);

        entity.setPitch(pitch);
        entity.setYaw(yaw);
        entity.bodyYaw = bodyYaw;
        entity.headYaw = headYaw;
    }

    public static void drawEntity(DrawContext context, int x, int y, int size, Quaternionf rotation, Entity entity) {
        context.getMatrices().push();
        context.getMatrices().translate(x, y, 450);
        context.getMatrices().multiplyPositionMatrix(new Matrix4f().scaling(size, size, size));
        context.getMatrices().multiply(rotation);

        DiffuseLighting.method_34742();

        EntityRenderDispatcher dispatcher = MinecraftClient.getInstance().getEntityRenderDispatcher();
        dispatcher.setRenderShadows(false);
        dispatcher.render(entity, 0, 0, 0, 0, 1, context.getMatrices(), context.getVertexConsumers(), SHADOW_LIGHT_COLOR);
        dispatcher.setRenderShadows(true);

        context.getMatrices().pop();
        DiffuseLighting.enableGuiDepthLighting();
    }
}
