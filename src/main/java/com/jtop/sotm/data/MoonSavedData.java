package com.jtop.sotm.data;

import com.jtop.sotm.moon.MoonPhase;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;

public class MoonSavedData extends SavedData {

    private static final String DATA_NAME = "sonofthemoon_moon_phase";
    private int currentPhaseId = 0;

    public MoonSavedData() {}

    public static MoonSavedData load(CompoundTag tag, HolderLookup.Provider provider) {
        MoonSavedData data = new MoonSavedData();
        data.currentPhaseId = tag.getInt("phase_id");
        return data;
    }

    @Override
    public CompoundTag save(CompoundTag tag, HolderLookup.Provider provider) {
        tag.putInt("phase_id", currentPhaseId);
        return tag;
    }

    public void setPhase(MoonPhase phase) {
        this.currentPhaseId = phase.getId();
        setDirty();
    }

    public MoonPhase getPhase() {
        return MoonPhase.byId(currentPhaseId);
    }

    private static final Factory<MoonSavedData> FACTORY = new Factory<>(
            MoonSavedData::new,
            MoonSavedData::load,
            null
    );

    public static MoonSavedData get(ServerLevel level) {
        DimensionDataStorage storage = level.getDataStorage();
        return storage.computeIfAbsent(FACTORY, DATA_NAME);
    }
}