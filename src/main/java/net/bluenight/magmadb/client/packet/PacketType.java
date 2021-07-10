package net.bluenight.magmadb.client.packet;

import net.bluenight.magmadb.client.packet.clientbound.*;
import net.bluenight.magmadb.client.packet.serverbound.*;

public class PacketType {
    public static final Class<?>[] INBOUND = new Class<?>[8];
    public static final Class<?>[] OUTBOUND = new Class<?>[8];

    static {
        registerInbound();
        registerOutbound();
    }

    private static void registerInbound() {
        INBOUND[0x0] = CPacketLogin.class;
        INBOUND[0x1] = CPacketFindDatabase.class;
        INBOUND[0x2] = CPacketCreateDatabase.class;
        INBOUND[0x3] = CPacketDeleteDatabase.class;
        INBOUND[0x4] = CPacketFindCollection.class;
        INBOUND[0x5] = CPacketCreateCollection.class;
        INBOUND[0x6] = CPacketFindElement.class;
        INBOUND[0x7] = CPacketUpdateElement.class;
    }

    private static void registerOutbound() {
        OUTBOUND[0x0] = SPacketLogin.class;
        OUTBOUND[0x1] = SPacketFindDatabase.class;
        OUTBOUND[0x2] = SPacketCreateDatabase.class;
        OUTBOUND[0x3] = SPacketDeleteDatabase.class;
        OUTBOUND[0x4] = SPacketFindCollection.class;
        OUTBOUND[0x5] = SPacketCreateCollection.class;
        OUTBOUND[0x6] = SPacketFindElement.class;
        OUTBOUND[0x7] = SPacketUpdateElement.class;
    }

    public static int getPacketID(Packet packet) {
        for (int i = 0; i < OUTBOUND.length; i++) {
            if (OUTBOUND[i].equals(packet.getClass())) {
                return i;
            }
        }
        throw new IllegalArgumentException("No such packet id corresponding for packet " + packet);
    }
}
