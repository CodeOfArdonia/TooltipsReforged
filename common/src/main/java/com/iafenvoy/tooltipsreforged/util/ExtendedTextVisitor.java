package com.iafenvoy.tooltipsreforged.util;

import net.minecraft.text.*;

public class ExtendedTextVisitor {
    public static Text getText(OrderedText text) {
        TextVisitor visitor = new TextVisitor();
        text.accept(visitor);
        return visitor.getText();
    }

    public static String getString(Text text) {
        StringVisitor visitor = new StringVisitor();
        text.asOrderedText().accept(visitor);
        return visitor.getString();
    }

    private static class TextVisitor implements CharacterVisitor {
        private final MutableText text = Text.empty();

        @Override
        public boolean accept(int index, Style style, int codePoint) {
            this.text.append(Text.literal(new String(Character.toChars(codePoint))).setStyle(style));
            return true;
        }

        public Text getText() {
            return this.text;
        }
    }

    private static class StringVisitor implements CharacterVisitor {
        private final StringBuilder sb = new StringBuilder();

        @Override
        public boolean accept(int index, Style style, int codePoint) {
            this.sb.append(new String(Character.toChars(codePoint)));
            return true;
        }

        public String getString() {
            return this.sb.toString();
        }
    }
}
