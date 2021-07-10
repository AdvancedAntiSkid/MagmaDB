package net.bluenight.magmadb.server.packet.clientbound;

import com.google.gson.JsonObject;
import net.bluenight.magmadb.server.packet.Packet;
import net.bluenight.magmadb.wrapper.EnumInteractResult;

public class PacketOutFindElement implements Packet {
    private int transactionId;
    private JsonObject data;
    private EnumInteractResult result;

    public PacketOutFindElement(int transactionId, JsonObject data, EnumInteractResult result) {
        this.transactionId = transactionId;
        this.data = data;
        this.result = result;
    }

    @Override
    public void read(JsonObject json) {
        transactionId = json.get("transactionId").getAsInt();
        data = json.getAsJsonObject("data");
        result = EnumInteractResult.valueOf(json.get("result").getAsInt());
    }

    @Override
    public void write(JsonObject json) {
        json.addProperty("transactionId", transactionId);
        json.add("data", data);
        json.addProperty("result", result.getId());
    }

    public int getTransactionId() {
        return transactionId;
    }

    public JsonObject getData() {
        return data;
    }

    public EnumInteractResult getResult() {
        return result;
    }
}
