package net.bluenight.magmadb.client.packet.clientbound;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import net.bluenight.magmadb.client.packet.InteractionResult;
import net.bluenight.magmadb.client.packet.Packet;
import net.bluenight.magmadb.wrapper.EnumInteractResult;

public class CPacketFindElement implements Packet, InteractionResult {
    private int transactionId;
    private EnumInteractResult result;
    private JsonObject data;

    @Override
    public void read(JsonObject json) {
        transactionId = json.get("transactionId").getAsInt();
        result = EnumInteractResult.valueOf(json.get("result").getAsInt());
        JsonElement element = json.get("data");
        data = element instanceof JsonNull ? null : element.getAsJsonObject();
    }

    @Override
    public void write(JsonObject json) {
        json.addProperty("transactionId", transactionId);
        json.addProperty("result", result.getId());
        json.add("data", data);
    }

    public int getTransactionId() {
        return transactionId;
    }

    @Override
    public EnumInteractResult getResult() {
        return result;
    }

    public JsonObject getData() {
        return data;
    }
}
