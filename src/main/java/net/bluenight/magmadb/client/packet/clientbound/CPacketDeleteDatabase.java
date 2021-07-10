package net.bluenight.magmadb.client.packet.clientbound;

import com.google.gson.JsonObject;
import net.bluenight.magmadb.client.packet.InteractionResult;
import net.bluenight.magmadb.client.packet.Packet;
import net.bluenight.magmadb.wrapper.EnumInteractResult;

public class CPacketDeleteDatabase implements Packet, InteractionResult {
    private String name;
    private EnumInteractResult result;

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

    @Override
    public EnumInteractResult getResult() {
        return result;
    }
}
