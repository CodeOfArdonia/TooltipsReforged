package com.iafenvoy.tooltipsreforged.component;

import com.iafenvoy.tooltipsreforged.config.TooltipReforgedConfig;
import com.iafenvoy.tooltipsreforged.render.TooltipsRenderHelper;
import com.iafenvoy.tooltipsreforged.util.ExtendedTextVisitor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.tooltip.OrderedTextTooltipComponent;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.Equipment;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;

import java.util.LinkedList;
import java.util.List;

public class EquipmentCompareComponent extends StandaloneComponent {
    private final List<TooltipComponent> components = new LinkedList<>();
    private final ItemStack equipped;

    public EquipmentCompareComponent(ItemStack stack) {
        super(stack);
        if (!TooltipReforgedConfig.INSTANCE.tooltip.equipmentCompareTooltip.getValue() || !(this.stack.getItem() instanceof Equipment equipment)) {
            this.equipped = ItemStack.EMPTY;
            return;
        }
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        assert player != null;
        this.equipped = player.getEquippedStack(equipment.getSlotType());
        if (this.equipped.isEmpty() || ItemStack.areItemsAndComponentsEqual(this.equipped, this.stack)) return;
        for (Text text : this.equipped.getTooltip(Item.TooltipContext.DEFAULT, player, TooltipType.BASIC))
            if (!TooltipReforgedConfig.INSTANCE.misc.removeEmptyLines.getValue() || !ExtendedTextVisitor.getText(text.asOrderedText()).getString().isEmpty())
                this.components.add(new OrderedTextTooltipComponent(text.asOrderedText()));
        if (!this.components.isEmpty()) this.components.removeFirst();
        List<TooltipComponent> headers = new LinkedList<>();
        headers.add(new OrderedTextTooltipComponent(Text.translatable("tooltip.tooltips_reforged.currently_equipped").asOrderedText()));
        headers.add(new HeaderComponent(this.equipped, null));
        if (this.equipped.getItem().isEnchantable(this.equipped))
            headers.add(new EnchantmentsComponent(this.equipped.getEnchantments()));
        this.components.addAll(0, headers);
        this.components.add(new DurabilityComponent(this.equipped));
    }

    @Override
    public void render(DrawContext context, TextRenderer textRenderer, int x, int y, int z) {
        if (this.components.isEmpty()) return;
        TooltipsRenderHelper.ResolveResult result = TooltipsRenderHelper.resolveTooltips(textRenderer, this.components);
        if (result.pages().isEmpty()) return;
        TooltipsRenderHelper.Page page = result.pages().getFirst();
        int height = page.getHeight();
        TooltipsRenderHelper.drawWithResult(result, this.equipped, context, textRenderer, x, y - height - 20);
    }
}
