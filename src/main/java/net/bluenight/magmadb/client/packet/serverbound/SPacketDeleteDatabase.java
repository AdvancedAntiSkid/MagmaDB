package net.bluenight.magmadb.client.packet.serverbound;

import com.google.gson.JsonObject;
import net.bluenight.magmadb.client.packet.Packet;

public class SPacketDeleteDatabase implements Packet {
    private String name;

    public SPacketDeleteDatabase(String name) {
        this.name = name;
    }

    @Override
    public void read(JsonObject json) {
        name = json.get("name").getAsString();
    }

    @Override
    public void write(JsonObject json) {
        json.addProperty("name", name);
    }

    public String getName() {
        return name;
    }
}
