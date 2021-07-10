package net.bluenight.magmadb.client.packet.serverbound;

import com.google.gson.JsonObject;
import net.bluenight.magmadb.client.packet.Packet;

public class SPacketFindCollection implements Packet {
    private String database;
    private String name;

    public SPacketFindCollection(String database, String name) {
        this.database = database;
        this.name = name;
    }

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

    @Override
    public String toString() {
        return "SPacketCollection{"
            + "database='" + database + '\''
            + "name='" + name + '\''
            + '}';
    }
}
