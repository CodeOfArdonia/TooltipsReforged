package com.iafenvoy.tooltipsreforged.util;

import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.Objects;

public class TranslationStringColorParser {
    public static int getColorFromTranslation(Text text) {
        return getColorFromTranslation(text.getString());
    }

    public static int getColorFromTranslation(String text) {
        char[] charArray = text.toCharArray();
        if (charArray.length < 2) return 0xFFFFFFFF;
        for (int i = 0; i < charArray.length - 1; i++) {
            if (charArray[i] == '§') {
                Formatting formatting = Formatting.byCode(charArray[i + 1]);
                int color = formatting == null ? 0xFFFFFFFF : Objects.requireNonNullElse(formatting.getColorValue(), 0xFFFFFFFF);
                if (color != 0xFFFFFFFF) return color;
            }
        }
        return 0xFFFFFFFF;
    }
}
