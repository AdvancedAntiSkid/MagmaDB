package net.bluenight.magmadb.server.packet.serverbound;

import com.google.gson.JsonObject;
import net.bluenight.magmadb.server.packet.Packet;

public class PacketInCollectionRequest implements Packet {
    private String database;
    private String name;

    @Override
    public void read(JsonObject json) {
        database = json.get("database").getAsString();
        name = json.get("name").getAsString();
    }

    @Override
    public void write(JsonObject json) {
        json.addProperty("database", database);
        json.addProperty("name", name);
    }

    public String getDatabase() {
        return database;
    }

    public String getName() {
        return name;
    }
}
