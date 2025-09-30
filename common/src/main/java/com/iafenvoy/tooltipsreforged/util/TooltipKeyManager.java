package com.iafenvoy.tooltipsreforged.util;

import com.iafenvoy.tooltipsreforged.TooltipReforgedClient;
import com.iafenvoy.tooltipsreforged.mixin.KeyBindingAccessor;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.client.util.InputUtil;

@Environment(EnvType.CLIENT)
public class TooltipKeyManager {
    private static final String CATEGORY = "category.%s.keys".formatted(TooltipReforgedClient.MOD_ID);
    public static final KeyBinding SHOW_SPECIFIC_INFO = new KeyBinding("keybinding.%s.show_specific_info".formatted(TooltipReforgedClient.MOD_ID), InputUtil.GLFW_KEY_RIGHT_SHIFT, CATEGORY);
    public static final KeyBinding SHOW_NBT_SPAWN_EGG = new KeyBinding("keybinding.%s.show_nbt_spawn_egg".formatted(TooltipReforgedClient.MOD_ID), InputUtil.GLFW_KEY_RIGHT_ALT, CATEGORY);
    public static final KeyBinding SHOW_ITEM_TAG = new KeyBinding("keybinding.%s.show_item_tag".formatted(TooltipReforgedClient.MOD_ID), InputUtil.GLFW_KEY_RIGHT_CONTROL, CATEGORY);
    private boolean pressing;
    private Pressed pressed;

    private static InputUtil.Key getBoundKey(KeyBinding binding) {
        return ((KeyBindingAccessor) binding).getBoundKey();
    }

    private static boolean pressed(KeyBinding binding) {
        return !isUnknownKey(binding) && InputUtil.isKeyPressed(MinecraftClient.getInstance().getWindow().getHandle(), getBoundKey(binding).getCode());
    }

    private static boolean isUnknownKey(KeyBinding binding) {
        return getBoundKey(binding) == InputUtil.UNKNOWN_KEY;
    }

    public void renderTick() {
        if (pressed(SHOW_ITEM_TAG)) {
            if (!this.pressing) {
                this.pressed = this.pressed == Pressed.ITEM_TAG ? Pressed.NONE : Pressed.ITEM_TAG;
                this.pressing = true;
            }
        } else if (pressed(SHOW_SPECIFIC_INFO)) {
            if (!this.pressing) {
                this.pressed = this.pressed == Pressed.SPECIFIC ? Pressed.NONE : Pressed.SPECIFIC;
                this.pressing = true;
            }
        } else if (pressed(SHOW_NBT_SPAWN_EGG)) {
            if (!this.pressing) {
                this.pressed = this.pressed == Pressed.NBT ? Pressed.NONE : Pressed.NBT;
                this.pressing = true;
            }
        } else this.pressing = false;
    }

    public PressState itemTag() {
        return isUnknownKey(SHOW_ITEM_TAG) ? PressState.HIDE : this.pressed == Pressed.ITEM_TAG ? PressState.PRESSED : PressState.UNPRESSED;
    }

    public PressState specific() {
        return isUnknownKey(SHOW_SPECIFIC_INFO) ? PressState.HIDE : this.pressed == Pressed.SPECIFIC ? PressState.PRESSED : PressState.UNPRESSED;
    }

    public PressState nbt() {
        return isUnknownKey(SHOW_NBT_SPAWN_EGG) ? PressState.HIDE : this.pressed == Pressed.NBT ? PressState.PRESSED : PressState.UNPRESSED;
    }

    public static String itemTagKeyTranslation() {
        return I18n.translate(SHOW_ITEM_TAG.getBoundKeyTranslationKey());
    }

    public static String specificKeyTranslation() {
        return I18n.translate(SHOW_SPECIFIC_INFO.getBoundKeyTranslationKey());
    }

    public static String nbtKeyTranslation() {
        return I18n.translate(SHOW_NBT_SPAWN_EGG.getBoundKeyTranslationKey());
    }

    private enum Pressed {
        NONE, ITEM_TAG, SPECIFIC, NBT
    }

    public enum PressState {
        HIDE(false, false), UNPRESSED(true, false), PRESSED(true, true);

        private final boolean show, showDetail;

        PressState(boolean show, boolean showDetail) {
            this.show = show;
            this.showDetail = showDetail;
        }

        public boolean show() {
            return this.show;
        }

        public boolean showDetail() {
            return this.showDetail;
        }
    }
}
