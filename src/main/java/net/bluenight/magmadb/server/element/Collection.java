package net.bluenight.magmadb.server.element;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

public class Collection {
    private final String name;
    private final List<Element> elements;

    public Collection(String name) {
        this.name = name;
        elements = new ArrayList<>();
    }

    public static Collection fromJson(JsonObject json) {
        Collection collection = new Collection(json.get("name").getAsString());
        for (JsonElement element : json.getAsJsonArray("elements")) {
            collection.elements.add(Element.fromJson(element.getAsJsonObject()));
        }
        return collection;
    }
}
