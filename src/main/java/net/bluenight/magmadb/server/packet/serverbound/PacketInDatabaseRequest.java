package net.bluenight.magmadb.server.packet.serverbound;

import com.google.gson.JsonObject;
import net.bluenight.magmadb.server.packet.Packet;

public class PacketInDatabaseRequest implements Packet {
    private String name;

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
