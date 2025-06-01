package com.iafenvoy.tooltipsreforged.forge;

import com.iafenvoy.jupiter.util.ReflectUtil;
import com.iafenvoy.tooltipsreforged.EntryPointLoader;
import com.iafenvoy.tooltipsreforged.api.TooltipsProvider;
import com.iafenvoy.tooltipsreforged.api.TooltipsReforgeEntrypoint;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.forgespi.language.ModFileScanData;
import org.objectweb.asm.Type;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ForgeEntryPointLoader extends EntryPointLoader {
    public static void init() {
        INSTANCE = new ForgeEntryPointLoader();
    }

    private static <T> List<T> combine(List<T> l1, List<T> l2) {
        l1.addAll(l2);
        return l1;
    }

    @Override
    protected List<TooltipsReforgeEntrypoint> loadEntries() {
        final Type type = Type.getType(TooltipsProvider.class);
        return ModList.get()
                .getAllScanData()
                .stream()
                .map(scanData -> scanData.getAnnotations()
                        .stream()
                        .filter(x -> x.annotationType().equals(type))
                        .map(ModFileScanData.AnnotationData::memberName)
                        .map(ReflectUtil::getClassUnsafely)
                        .filter(Objects::nonNull)
                        .map(ReflectUtil::constructUnsafely)
                        .filter(x -> x instanceof TooltipsReforgeEntrypoint)
                        .map(TooltipsReforgeEntrypoint.class::cast)
                        .toList())
                .reduce(new ArrayList<>(), ForgeEntryPointLoader::combine);
    }

    @Override
    public boolean isModLoaded(String modId) {
        return ModList.get().isLoaded(modId);
    }
}
