package com.iafenvoy.tooltipsreforged;

import com.iafenvoy.tooltipsreforged.api.TooltipsReforgeEntrypoint;

import java.util.List;

public abstract class EntryPointLoader {
    public static EntryPointLoader INSTANCE;
    private List<TooltipsReforgeEntrypoint> entries;

    public List<TooltipsReforgeEntrypoint> getEntries() {
        if (this.entries == null)
            this.entries = this.loadEntries();
        return this.entries;
    }

    protected abstract List<TooltipsReforgeEntrypoint> loadEntries();
}
