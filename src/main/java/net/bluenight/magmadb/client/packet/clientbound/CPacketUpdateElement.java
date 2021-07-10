package net.bluenight.magmadb.client.packet.clientbound;

import com.google.gson.JsonObject;
import net.bluenight.magmadb.client.packet.InteractionResult;
import net.bluenight.magmadb.client.packet.Packet;
import net.bluenight.magmadb.wrapper.EnumInteractResult;

public class CPacketUpdateElement implements Packet, InteractionResult {
    private int transactionId;
    private EnumInteractResult result;

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

    @Override
    public EnumInteractResult getResult() {
        return result;
    }
}
