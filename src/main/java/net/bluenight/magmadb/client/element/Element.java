package net.bluenight.magmadb.client.element;

import com.google.gson.*;

import java.util.function.Consumer;

public class Element extends MagmaObject {
    private final Collection collection;
    private final String id;

    public Element(Collection collection, String id) {
        this.collection = collection;
        this.id = id;
    }

    public boolean update(String update) {
        return collection.updateOne(String.format("e._id = '%s'", id), update);
    }

    public void update(String update, Consumer<Boolean> action) {
        collection.updateOne(String.format("e._id = '%s'", id), update, action);
    }

    public void delete() {

    }

    public Collection getCollection() {
        return collection;
    }

    public String getId() {
        return id;
    }

    public static Element fromJson(Collection collection, JsonObject json) {
        Element element = new Element(collection, json.get("_id").getAsString());
        for (String key : json.keySet()) {
            JsonElement e = json.get(key);
            if (e instanceof JsonNull)
                element.add(key, MagmaNull.INSTANCE);
            else if (e instanceof JsonPrimitive)
                element.add(key, MagmaPrimitive.fromJson((JsonPrimitive) e));
            else if (e instanceof JsonObject)
                element.add(key, MagmaObject.fromJson((JsonObject) e));
            else if (e instanceof JsonArray)
                element.add(key, MagmaArray.fromJson((JsonArray) e));
        }
        return element;
    }
}
