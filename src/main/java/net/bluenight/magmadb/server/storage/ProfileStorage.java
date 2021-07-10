package net.bluenight.magmadb.server.storage;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.File;
import java.io.IOException;

public class ProfileStorage extends Storage {
    public ProfileStorage(File file) throws IOException {
        super(file);
    }

    @Override
    public void create(JsonObject json) {
        json.add("profiles", new JsonArray());
    }

    public Profile getProfile(String name) {
        for (JsonElement element : load().getAsJsonArray("profiles")) {
            JsonObject json = element.getAsJsonObject();
            if (json.get("username").getAsString().equals(name)) {
                return new Profile(name, json.get("password").getAsString());
            }
        }
        return null;
    }
}
