package net.bluenight.magmadb.client.packet;

import net.bluenight.magmadb.client.packet.clientbound.*;

public interface Check {
    boolean test(Packet packet);

    static Check testDatabaseFind(String name) {
        return packet -> packet instanceof CPacketFindDatabase && ((CPacketFindDatabase) packet).getName().equals(name);
    }

    static Check testDatabaseCreate(String name) {
        return packet -> packet instanceof CPacketCreateDatabase && ((CPacketCreateDatabase) packet).getName().equals(name);
    }

    static Check testDatabaseDelete(String name) {
        return packet -> packet instanceof CPacketDeleteDatabase && ((CPacketDeleteDatabase) packet).getName().equals(name);
    }

    static Check testCollectionFind(String database, String name) {
        return packet -> packet instanceof CPacketFindCollection
            && ((CPacketFindCollection) packet).getDatabase().equals(database)
            && ((CPacketFindCollection) packet).getName().equals(name);
    }

    static Check testCollectionCreate(String database, String name) {
        return packet -> packet instanceof CPacketCreateCollection
            && ((CPacketCreateCollection) packet).getDatabase().equals(database)
            && ((CPacketCreateCollection) packet).getName().equals(name);
    }

    static Check testCollectionDelete(String database, String name) {
        return packet -> packet instanceof CPacketDeleteCollection
            && ((CPacketDeleteCollection) packet).getDatabase().equals(database)
            && ((CPacketDeleteCollection) packet).getName().equals(name);
    }

    static Check testElementFind(int id) {
        return packet -> packet instanceof CPacketFindElement
            && ((CPacketFindElement) packet).getTransactionId() == id;
    }

    static Check testElementUpdate(int id) {
        return packet -> packet instanceof CPacketUpdateElement
            && ((CPacketUpdateElement) packet).getTransactionId() == id;
    }
}
