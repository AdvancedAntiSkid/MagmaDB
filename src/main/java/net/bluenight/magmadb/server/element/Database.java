package net.bluenight.magmadb.server.element;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

public class Database {
    private final String name;
    private final List<Collection> collections;

    public Database(String name) {
        this.name = name;
        collections = new ArrayList<>();
    }

    public static Database fromJson(JsonObject json) {
        Database database = new Database(json.get("name").getAsString());
        for (JsonElement element : json.getAsJsonArray("collections")) {
            database.collections.add(Collection.fromJson(element.getAsJsonObject()));
        }
        return database;
    }

    public String getName() {
        return name;
    }
}
