package net.bluenight.magmadb.wrapper;

public enum EnumInteractResult {
    FOUND(0), CREATED(1), UPDATED(2), PUT(3), DELETED(4), INVALID_ACCESS(5), INVALID_SYNTAX(6), NOT_FOUND(7), DUPLICATE(8), UNKNOWN(-1);

    private final int id;

    EnumInteractResult(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static EnumInteractResult valueOf(int id) {
        for (EnumInteractResult result : values()) {
            if (result.id == id) {
                return result;
            }
        }
        return UNKNOWN;
    }
}
