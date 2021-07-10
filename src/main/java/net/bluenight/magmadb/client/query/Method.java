package net.bluenight.magmadb.client.query;


public class Method {
    public static Method length(String key) {
        return null;
    }

    public enum MethodType {
        LENGTH  (1, "$length"),
        UNKNOWN (-1, "");

        private final int id;
        private final String name;

        MethodType(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public static MethodType valueOf(int id) {
            for (MethodType type : values()) {
                if (type.id == id) {
                    return type;
                }
            }
            return UNKNOWN;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }
    }
}
