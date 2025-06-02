package com.iafenvoy.tooltipsreforged.util;

import java.util.List;

public class RandomHelper {
    private static final int INTERVAL = 1000;//ms

    public static <T> T pick(List<T> list, T empty) {
        if (list.isEmpty()) return empty;
        return list.get((int) (System.currentTimeMillis() / INTERVAL) % list.size());
    }
}
