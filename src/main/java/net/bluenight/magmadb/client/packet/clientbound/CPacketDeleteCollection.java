package net.bluenight.magmadb.client.packet.clientbound;

import com.google.gson.JsonObject;
import net.bluenight.magmadb.client.packet.InteractionResult;
import net.bluenight.magmadb.client.packet.Packet;
import net.bluenight.magmadb.wrapper.EnumInteractResult;

public class CPacketDeleteCollection implements Packet, InteractionResult {
    private String database;
    private String name;
    private EnumInteractResult result;

    @Override
    public void read(JsonObject json) {
        database = json.get("database").getAsString();
        name = json.get("name").toString();
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

    @Override
    public EnumInteractResult getResult() {
        return result;
    }

    @Override
    public String toString() {
        return "CPacketDeleteCollection{"
            + "database='" + database + '\''
            + ", name='" + name + '\''
            + ", result=" + result
            + '}';
    }
}
