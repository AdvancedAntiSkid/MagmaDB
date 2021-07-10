package net.bluenight.magmadb.server.packet;

import net.bluenight.magmadb.server.packet.clientbound.*;
import net.bluenight.magmadb.server.packet.serverbound.*;

public class PacketType {
    public static final Class<?>[] INBOUND = new Class<?>[8];
    public static final Class<?>[] OUTBOUND = new Class<?>[8];

    static {
        registerInbound();
        registerOutbound();
    }

    private static void registerInbound() {
        INBOUND[0x0] = PacketInAuthenticate.class;
        INBOUND[0x1] = PacketInDatabaseRequest.class;
        INBOUND[0x2] = PacketInCreateDatabase.class;
        INBOUND[0x3] = PacketInRemoveDatabase.class;
        INBOUND[0x4] = PacketInCollectionRequest.class;
        INBOUND[0x5] = PacketInCreateCollection.class;
        INBOUND[0x6] = PacketInFindElement.class;
        INBOUND[0x7] = PacketInUpdateElement.class;
    }

    private static void registerOutbound() {
        OUTBOUND[0x0] = PacketOutAuthenticate.class;
        OUTBOUND[0x1] = PacketOutDatabaseRequest.class;
        OUTBOUND[0x2] = PacketOutCreateDatabase.class;
        OUTBOUND[0x3] = PacketOutRemoveDatabase.class;
        OUTBOUND[0x4] = PacketOutCollectionRequest.class;
        OUTBOUND[0x5] = PacketOutCreateCollection.class;
        OUTBOUND[0x6] = PacketOutFindElement.class;
        OUTBOUND[0x7] = PacketOutUpdateElement.class;
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
