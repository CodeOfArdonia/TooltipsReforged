package com.iafenvoy.tooltipsreforged.util;

import net.minecraft.text.*;

public class ExtendedTextVisitor implements CharacterVisitor {
    private final MutableText text = Text.empty();

    @Override
    public boolean accept(int index, Style style, int codePoint) {
        String car = new String(Character.toChars(codePoint));
        this.text.append(Text.literal(car).setStyle(style));
        return true;
    }

    public Text getText() {
        return this.text;
    }

    public static Text get(OrderedText text) {
        ExtendedTextVisitor visitor = new ExtendedTextVisitor();
        text.accept(visitor);
        return visitor.getText();
    }
}
