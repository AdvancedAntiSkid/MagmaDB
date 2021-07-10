package net.bluenight.magmadb.client.query;

import com.google.gson.JsonObject;

public class Filter {
    // a == b
    public static Filter eq(String key, Object value) {
        return null;
    }

    // a != b
    public static Filter notEq(String key, Object value) {
        return null;
    }

    // a >= b
    public static Filter moreEq(String key, Object value) {
        return null;
    }

    // a <= b
    public static Filter lessEq(String key, Object value) {
        return null;
    }

    // a > b
    public static Filter more(String key, Object value) {
        return null;
    }

    // a < b
    public static Filter less(String key, Object value) {
        return null;
    }

    private final FilterType type;
    private final Object key, value;

    public Filter(FilterType type, Object key, Object value) {
        this.type = type;
        this.key = key;
        this.value = value;
    }

    public JsonObject build() {
        JsonObject filter = new JsonObject();
        JsonObject json = new JsonObject();

        filter.add(type.getName(), json);
        return filter;
    }

    public FilterType getType() {
        return type;
    }

    public Object getKey() {
        return key;
    }

    public Object getValue() {
        return value;
    }

    public enum FilterType {
        EQUALS      (1, "$equals"),
        NOT_EQUALS  (2, "$not_equals"),
        MORE_EQUALS (3, "$more_equals"),
        LESS_EQUALS (4, "$less_equals"),
        MORE        (5, "$more"),
        LESS        (6, "$less"),
        UNKNOWN     (-1,"");

        private final int id;
        private final String name;

        FilterType(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public static FilterType valueOf(int id) {
            for (FilterType type : values()) {
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
