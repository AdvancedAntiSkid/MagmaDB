package net.bluenight.magmadb.client.packet;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.bluenight.magmadb.client.Client;
import net.bluenight.magmadb.client.packet.callback.PacketCallbackHandler;
import net.bluenight.magmadb.client.packet.clientbound.CPacketLogin;
import net.bluenight.magmadb.wrapper.EnumAuthResult;

public class PacketHandler {
    private final Client client;
    private final PacketCallbackHandler callbackHandler;

    public PacketHandler(Client client, PacketCallbackHandler callbackHandler) {
        this.client = client;
        this.callbackHandler = callbackHandler;
    }

    public void read(String data) throws Exception {
        // TODO implement encryption
        try {
            JsonObject json = (JsonObject) JsonParser.parseString(data);
            int id = json.get("id").getAsInt();

            Packet packet = (Packet) PacketType.INBOUND[id].newInstance();
            packet.read(json);

            // System.err.println("[CLIENT IN] " + packet);

            callbackHandler.handle(packet);
            handle(packet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handle(Packet packet) {
        if (packet instanceof CPacketLogin)
            onAuthenticate((CPacketLogin) packet);
    }

    private void onAuthenticate(CPacketLogin packet) {
        System.out.println("[Client] authenticating " + packet);
        if (packet.getResult() != EnumAuthResult.SUCCESS) {
            System.err.println("Unable to authenticate: " + packet.getResult());
        }
    }

    public String write(JsonObject data, Packet packet) {
        // TODO implement encryption
        data.addProperty("id", PacketType.getPacketID(packet));
        packet.write(data);

        return data.toString();
    }
}
