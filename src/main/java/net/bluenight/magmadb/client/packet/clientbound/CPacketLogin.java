package net.bluenight.magmadb.client.packet.clientbound;

import com.google.gson.JsonObject;
import net.bluenight.magmadb.client.packet.Packet;
import net.bluenight.magmadb.wrapper.EnumAuthResult;

public class CPacketLogin implements Packet {
    private EnumAuthResult result;

    @Override
    public void read(JsonObject json) {
        result = EnumAuthResult.fromId(json.get("result").getAsInt());
    }

    @Override
    public void write(JsonObject json) {
        json.addProperty("result", result.getId());
    }

    public EnumAuthResult getResult() {
        return result;
    }

    @Override
    public String toString() {
        return "CPacketLogin{"
            + "result=" + result
            + '}';
    }
}
