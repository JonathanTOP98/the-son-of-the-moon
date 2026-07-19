package com.jtop.sotm.moon;

public enum MoonPhase {
    NORMAL(0, "normal"),
    BLUE_MOON(1, "blue_moon"),
    CALMA(2, "calma"),
    TRISTE(3, "triste"),
    BLOOD_MOON(4, "blood_moon"),
    DESAPARICION(5, "desaparicion");

    private final int id;
    private final String name;

    MoonPhase(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() { return id; }
    public String getName() { return name; }

    public String getTranslationKey() {
        return "moon.phase." + name;
    }

    public String getLoreKey() {
        return "moon.phase." + name + ".lore";
    }

    public static MoonPhase byId(int id) {
        for (MoonPhase phase : values()) {
            if (phase.id == id) return phase;
        }
        return NORMAL;
    }

    public static MoonPhase byName(String name) {
        for (MoonPhase phase : values()) {
            if (phase.name.equalsIgnoreCase(name)) return phase;
        }
        return NORMAL;
    }
}