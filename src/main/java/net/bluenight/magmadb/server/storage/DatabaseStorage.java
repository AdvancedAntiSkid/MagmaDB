package net.bluenight.magmadb.server.storage;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DatabaseStorage extends Storage {
    private JsonArray databases;

    public DatabaseStorage(File file) throws IOException {
        super(file);
    }

    @Override
    public void create(JsonObject json) {
        json.add("databases", new JsonArray());
    }

    public JsonObject getDatabase(String name) {
        for (JsonElement element : load().getAsJsonArray("databases")) {
            JsonObject json = element.getAsJsonObject();
            if (json.get("name").getAsString().equals(name)) {
                return json;
            }
        }
        return null;
    }

    public void createDatabase(String name) {
        JsonObject database = new JsonObject();
        database.addProperty("name", name);
        database.add("collections", new JsonArray());
        JsonObject json = load();
        json.getAsJsonArray("databases").add(database);
        save(json);
    }

    public void removeDatabase(String name) {
        JsonObject json = load();
        JsonArray array = json.getAsJsonArray("databases");
        Iterator<JsonElement> iterator = array.iterator();
        while (iterator.hasNext()) {
            JsonObject database = iterator.next().getAsJsonObject();
            if (database.get("name").getAsString().equals(name)) {
                iterator.remove();
            }
        }
        save(json);
    }

    // Don't mind these for-loop stacks lol

    public JsonObject getCollection(String database, String name) {
        for (JsonElement element : load().getAsJsonArray("databases")) {
            JsonObject json = element.getAsJsonObject();
            if (json.get("name").getAsString().equals(database)) {
                for (JsonElement col : json.getAsJsonArray("collections")) {
                    JsonObject colJson = col.getAsJsonObject();
                    if (colJson.get("name").getAsString().equals(name)) {
                        return colJson;
                    }
                }
            }
        }
        return null;
    }

    public void createCollection(String database, String name) {
        JsonObject collection = new JsonObject();
        collection.addProperty("name", name);
        collection.add("elements", new JsonArray());
        JsonObject json = load();
        for (JsonElement element : json.getAsJsonArray("databases")) {
            JsonObject dbObject = element.getAsJsonObject();
            if (dbObject.get("name").getAsString().equals(database)) {
                dbObject.getAsJsonArray("collections").add(collection);
            }
        }
        save(json);
    }

    public List<JsonObject> findElements(String database, String collection) {
        for (JsonElement dbElement : load().getAsJsonArray("databases")) {
            JsonObject dbJson = dbElement.getAsJsonObject();
            if (dbJson.get("name").getAsString().equals(database)) {
                for (JsonElement colElement : dbJson.getAsJsonArray("collections")) {
                    JsonObject colJson = colElement.getAsJsonObject();
                    if (colJson.get("name").getAsString().equals(collection)) {
                        List<JsonObject> elements = new ArrayList<>();
                        for (JsonElement elem : colJson.getAsJsonArray("elements")) {
                            elements.add(elem.getAsJsonObject());
                        }
                        return elements;
                    }
                }
            }
        }
        return null;
    }
}
