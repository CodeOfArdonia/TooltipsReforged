package com.iafenvoy.tooltipsreforged.component;

import com.iafenvoy.tooltipsreforged.config.TooltipReforgedConfig;
import com.iafenvoy.tooltipsreforged.config.mode.ArmorRenderMode;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.DiffuseLighting;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.*;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.passive.*;
import net.minecraft.item.*;
import net.minecraft.nbt.NbtCompound;
import org.joml.Matrix4f;
import org.joml.Quaternionf;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

@Environment(EnvType.CLIENT)
public class ModelViewerComponent extends BackgroundComponent {
    public static final Map<EntityBucketItem, Supplier<? extends EntityType<?>>> ENTITY_BUCKET_MAP = new HashMap<>();
    private static final float ROTATION_INCREMENT = 0.2f;
    private static float CURRENT_ROTATION = 0f;
    private static final int ENTITY_OFFSET = 40, SHADOW_LIGHT_COLOR = 15728880;

    public ModelViewerComponent(ItemStack stack) {
        super(stack);
    }

    @Override
    public void render(DrawContext context, int x, int y, int width, int height, int z, int page) throws Exception {
        super.render(context, x, y, width, height, z, page);
        if (page != 0) return;
        CURRENT_ROTATION = (CURRENT_ROTATION + ROTATION_INCREMENT) % 360;

        if (this.stack.getItem() instanceof Equipment || this.stack.getItem() instanceof SkullItem)
            switch ((ArmorRenderMode) TooltipReforgedConfig.INSTANCE.tooltip.armorTooltip.getValue()) {
                case PLAYER -> this.renderPlayer(context, x, y, z);
                case ARMOR_STAND -> this.renderArmorStand(context, x, y, z);
            }
        else if (this.stack.getItem() instanceof EntityBucketItem bucketItem) {
            if (TooltipReforgedConfig.INSTANCE.tooltip.bucketTooltip.getValue())
                this.renderBucketEntity(context, x, y, z, bucketItem);
        } else if (this.stack.getItem() instanceof SpawnEggItem spawnEggItem) {
            if (TooltipReforgedConfig.INSTANCE.tooltip.spawnEggTooltip.getValue())
                this.renderSpawnEggEntity(context, x, y, z, spawnEggItem);
        }
    }

    private void renderPlayer(DrawContext context, int x, int y, int z) throws Exception {
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        if (player == null) return;
        EquipmentSlot slot = LivingEntity.getPreferredEquipmentSlot(this.stack);
        ItemStack original = player.getEquippedStack(slot);
        player.equipStack(slot, this.stack);
        super.render(context, x - ENTITY_OFFSET - 25, y, 40, 70, z, -1);
        drawEntity(context, x - 45, y + 65, 35, -CURRENT_ROTATION, player);
        player.equipStack(slot, original);
    }

    private void renderArmorStand(DrawContext context, int x, int y, int z) throws Exception {
        ArmorStandEntity armorStand = new ArmorStandEntity(EntityType.ARMOR_STAND, MinecraftClient.getInstance().world);
        armorStand.equipStack(LivingEntity.getPreferredEquipmentSlot(this.stack), this.stack);
        super.render(context, x - ENTITY_OFFSET - 25, y, 40, 70, z, -1);
        armorStand.tick();
        drawEntity(context, x - 45, y + 65, 35, -CURRENT_ROTATION, armorStand);
    }

    private void renderBucketEntity(DrawContext context, int x, int y, int z, EntityBucketItem bucketItem) throws Exception {
        if (!ENTITY_BUCKET_MAP.containsKey(bucketItem)) return;
        EntityType<?> entityType = ENTITY_BUCKET_MAP.get(bucketItem).get();
        Entity entity = entityType.create(MinecraftClient.getInstance().world);

        if (entity instanceof Bucketable bucketable && entity instanceof LivingEntity livingEntity) {
            NbtCompound nbtComponent = this.stack.getOrCreateNbt();
            bucketable.copyDataFromNbt(nbtComponent);
            if (entityType == EntityType.TROPICAL_FISH) return;
            if (bucketable instanceof PufferfishEntity pufferfishEntity) pufferfishEntity.setPuffState(2);

            super.render(context, x - ENTITY_OFFSET - 70, y, 80, 40, z, -1);
            livingEntity.tick();
            drawEntity(context, x - 67, y + 40, 30, -CURRENT_ROTATION, livingEntity);
        }
    }

    private void renderSpawnEggEntity(DrawContext context, int x, int y, int z, SpawnEggItem spawnEggItem) throws Exception {
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
            super.render(context, x - ENTITY_OFFSET - 70, y, 80, 80, z, -1);
            livingEntity.tick();
            drawEntity(context, x - 67, y + 75, 40, -CURRENT_ROTATION, livingEntity);
        }
    }

    public static void drawEntity(DrawContext context, int centerX, int centerY, int maxSize, float rotationYaw, LivingEntity entity) {
        float pitch = entity.getPitch(), yaw = entity.getYaw(), bodyYaw = entity.bodyYaw, headYaw = entity.headYaw;
        entity.setPitch(0);
        entity.setYaw(rotationYaw);
        entity.bodyYaw = rotationYaw;
        entity.headYaw = rotationYaw;

        double boxX = entity.getBoundingBox().getXLength(), boxY = entity.getBoundingBox().getYLength(), scale = (boxX + boxY) / 2;
        Quaternionf correctionRotation = entity instanceof CodEntity || entity instanceof SalmonEntity ? new Quaternionf().rotateZ((float) Math.toRadians(-90)) : new Quaternionf().rotateX((float) Math.toRadians(180));
        Quaternionf combinedRotation = new Quaternionf().rotateY((float) Math.toRadians(rotationYaw)).mul(correctionRotation);
        if (entity instanceof SchoolingFishEntity) {
            centerY -= 20;
            scale *= 1.5;
        }
        drawEntity(context, centerX - boxX / 2, centerY - boxY / 2, (float) (maxSize / scale), combinedRotation, entity);

        entity.setPitch(pitch);
        entity.setYaw(yaw);
        entity.bodyYaw = bodyYaw;
        entity.headYaw = headYaw;
    }

    public static void drawEntity(DrawContext context, double x, double y, float size, Quaternionf rotation, Entity entity) {
        MatrixStack matrices = context.getMatrices();
        matrices.push();
        matrices.translate(x, y, 450);
        matrices.scale(-1, 1, 1);
        matrices.multiplyPositionMatrix(new Matrix4f().scaling(size, size, size));
        matrices.multiply(rotation);

        DiffuseLighting.method_34742();

        EntityRenderDispatcher dispatcher = MinecraftClient.getInstance().getEntityRenderDispatcher();
        dispatcher.setRenderShadows(false);
        dispatcher.render(entity, 0, 0, 0, 0, 1, context.getMatrices(), context.getVertexConsumers(), SHADOW_LIGHT_COLOR);
        dispatcher.setRenderShadows(true);

        matrices.pop();
        DiffuseLighting.enableGuiDepthLighting();
    }

}
