package com.iafenvoy.tooltipsreforged.component;

import com.iafenvoy.tooltipsreforged.config.TooltipReforgedConfig;
import com.iafenvoy.tooltipsreforged.config.mode.ArmorRenderMode;
import com.iafenvoy.tooltipsreforged.render.RenderHelper;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.item.Equipment;
import net.minecraft.item.ItemStack;

@Environment(EnvType.CLIENT)
public class EquipmentViewerComponent extends StandaloneComponent implements RenderHelper {
    private static final float ROTATION_INCREMENT = 0.2f;
    private static float CURRENT_ROTATION = 0f;
    private static final int ENTITY_OFFSET = 40;

    public EquipmentViewerComponent(ItemStack stack) {
        super(stack);
    }

    @Override
    public void render(DrawContext context, TextRenderer textRenderer, int x, int y, int z) {
        CURRENT_ROTATION = (CURRENT_ROTATION + ROTATION_INCREMENT) % 360;
        switch (TooltipReforgedConfig.INSTANCE.tooltip.armorTooltip.getValue()) {
            case PLAYER -> this.renderPlayer(context, x, y, z);
            case ARMOR_STAND -> this.renderArmorStand(context, x, y, z);
        }
    }

    private void renderPlayer(DrawContext context, int x, int y, int z) {
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        if (player == null) return;
        EquipmentSlot slot = getPreferredEquipmentSlot(this.stack);
        ItemStack original = player.getEquippedStack(slot);
        player.equipStack(slot, this.stack);
        this.renderBackground(context, x - ENTITY_OFFSET - 25, y, 40, 70, z);
        this.drawEntity(context, x - 45, y + 65, 35, -CURRENT_ROTATION, player);
        player.equipStack(slot, original);
    }

    private void renderArmorStand(DrawContext context, int x, int y, int z) {
        ArmorStandEntity armorStand = new ArmorStandEntity(EntityType.ARMOR_STAND, MinecraftClient.getInstance().world);
        armorStand.equipStack(getPreferredEquipmentSlot(this.stack), this.stack);
        this.renderBackground(context, x - ENTITY_OFFSET - 25, y, 40, 70, z);
        armorStand.tick();
        this.drawEntity(context, x - 45, y + 65, 35, -CURRENT_ROTATION, armorStand);
    }

    private static EquipmentSlot getPreferredEquipmentSlot(ItemStack stack) {
        Equipment equipment = Equipment.fromStack(stack);
        return equipment != null ? equipment.getSlotType() : EquipmentSlot.MAINHAND;
    }
}
