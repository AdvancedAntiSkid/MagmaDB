package net.bluenight.magmadb.server.storage;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.*;

public abstract class Storage {
    private final File rootDir;

    public Storage(File file) throws IOException {
        this.rootDir = file;
        if (!file.isFile()) {
            file.createNewFile();
            FileWriter writer = new FileWriter(file);
            JsonObject json = new JsonObject();
            create(json);
            writer.write(json.toString());
            writer.close();
        }
    }

    public void create(JsonObject json) {
    }

    public JsonObject load() {
        try {
            return (JsonObject) JsonParser.parseReader(new FileReader(rootDir));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void save(JsonObject json) {
        try {
            FileWriter writer = new FileWriter(rootDir);
            writer.write(json.toString());
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public File getRootDir() {
        return rootDir;
    }
}
