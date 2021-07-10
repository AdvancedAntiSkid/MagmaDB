package net.bluenight.magmadb.server.packet.clientbound;

import com.google.gson.JsonObject;
import net.bluenight.magmadb.server.packet.Packet;
import net.bluenight.magmadb.wrapper.EnumInteractResult;

public class PacketOutCollectionRequest implements Packet {
    private String database;
    private String name;
    private EnumInteractResult result;

    public PacketOutCollectionRequest(String database, String name, EnumInteractResult result) {
        this.database = database;
        this.name = name;
        this.result = result;
    }

    @Override
    public void read(JsonObject json) {
        database = json.get("database").getAsString();
        name = json.get("name").getAsString();
        result = EnumInteractResult.valueOf(json.get("result").getAsInt());
    }

    @Override
    public void write(JsonObject json) {
        json.addProperty("database", database);
        json.addProperty("name", name);
        json.addProperty("result", result.getId());
    }

    public String getDatabase() {
        return database;
    }

    public String getName() {
        return name;
    }

    public EnumInteractResult getResult() {
        return result;
    }
}
