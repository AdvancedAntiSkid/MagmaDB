package net.bluenight.magmadb.server.packet.clientbound;

import com.google.gson.JsonObject;
import net.bluenight.magmadb.server.packet.Packet;
import net.bluenight.magmadb.wrapper.EnumInteractResult;

public class PacketOutUpdateElement implements Packet {
    private int transactionId;
    private EnumInteractResult result;

    public PacketOutUpdateElement(int transactionId, EnumInteractResult result) {
        this.transactionId = transactionId;
        this.result = result;
    }

    @Override
    public void read(JsonObject json) {
        transactionId = json.get("transactionId").getAsInt();
        result = EnumInteractResult.valueOf(json.get("result").getAsInt());
    }

    @Override
    public void write(JsonObject json) {
        json.addProperty("transactionId", transactionId);
        json.addProperty("result", result.getId());
    }

    public int getTransactionId() {
        return transactionId;
    }

    public EnumInteractResult getResult() {
        return result;
    }
}
