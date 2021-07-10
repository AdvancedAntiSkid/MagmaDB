package net.bluenight.magmadb.wrapper;

public enum EnumAuthResult {
    SUCCESS(0), UNKNOWN_PROFILE(1), INVALID_PASSWORD(2), UNKNOWN(-1);

    private final int id;

    EnumAuthResult(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static EnumAuthResult fromId(int id) {
        for (EnumAuthResult result : values()) {
            if (result.id == id) {
                return result;
            }
        }
        return UNKNOWN;
    }
}
