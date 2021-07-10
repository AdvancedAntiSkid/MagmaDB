package net.bluenight.magmadb.server.packet.clientbound;

import com.google.gson.JsonObject;
import net.bluenight.magmadb.server.packet.Packet;
import net.bluenight.magmadb.wrapper.EnumAuthResult;

public class PacketOutAuthenticate implements Packet {
    private EnumAuthResult result;

    public PacketOutAuthenticate(EnumAuthResult result) {
        this.result = result;
    }

    @Override
    public void read(JsonObject json) {
        result = EnumAuthResult.fromId(json.get("result").getAsInt());
    }

    @Override
    public void write(JsonObject json) {
        json.addProperty("result", result.getId());
    }
}
