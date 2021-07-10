package net.bluenight.magmadb.server.packet.clientbound;

import com.google.gson.JsonObject;
import net.bluenight.magmadb.server.packet.Packet;
import net.bluenight.magmadb.wrapper.EnumInteractResult;

public class PacketOutDatabaseRequest implements Packet {
    private String name;
    private EnumInteractResult result;

    public PacketOutDatabaseRequest(String name, EnumInteractResult result) {
        this.name = name;
        this.result = result;
    }

    @Override
    public void read(JsonObject json) {
        name = json.get("name").getAsString();
        result = EnumInteractResult.valueOf(json.get("result").getAsInt());
    }

    @Override
    public void write(JsonObject json) {
        json.addProperty("name", name);
        json.addProperty("result", result.getId());
    }

    public String getName() {
        return name;
    }

    public EnumInteractResult getResult() {
        return result;
    }
}
