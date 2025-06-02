package com.iafenvoy.tooltipsreforged.util;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.InputUtil;

public class TooltipKeyManager {
    private boolean pressing;
    private Pressed pressed;

    public void renderTick() {
        if (InputUtil.isKeyPressed(MinecraftClient.getInstance().getWindow().getHandle(), InputUtil.GLFW_KEY_LEFT_CONTROL)) {
            if (!this.pressing) {
                this.pressed = this.pressed == Pressed.CTRL ? Pressed.NONE : Pressed.CTRL;
                this.pressing = true;
            }
        } else if (InputUtil.isKeyPressed(MinecraftClient.getInstance().getWindow().getHandle(), InputUtil.GLFW_KEY_LEFT_SHIFT)) {
            if (!this.pressing) {
                this.pressed = this.pressed == Pressed.SHIFT ? Pressed.NONE : Pressed.SHIFT;
                this.pressing = true;
            }
        } else if (InputUtil.isKeyPressed(MinecraftClient.getInstance().getWindow().getHandle(), InputUtil.GLFW_KEY_LEFT_ALT)) {
            if (!this.pressing) {
                this.pressed = this.pressed == Pressed.ALT ? Pressed.NONE : Pressed.ALT;
                this.pressing = true;
            }
        } else this.pressing = false;
    }

    public boolean ctrl() {
        return this.pressed == Pressed.CTRL;
    }

    public boolean shift() {
        return this.pressed == Pressed.SHIFT;
    }

    public boolean alt() {
        return this.pressed == Pressed.ALT;
    }

    private enum Pressed {
        NONE, CTRL, SHIFT, ALT
    }
}
