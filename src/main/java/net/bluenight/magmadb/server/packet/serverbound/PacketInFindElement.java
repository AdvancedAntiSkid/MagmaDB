package net.bluenight.magmadb.server.packet.serverbound;

import com.google.gson.JsonObject;
import net.bluenight.magmadb.server.packet.Packet;

public class PacketInFindElement implements Packet {
    private int transactionId;
    private String query;
    private String database;
    private String collection;

    @Override
    public void read(JsonObject json) {
        transactionId = json.get("transactionId").getAsInt();
        query = json.get("query").getAsString();
        database = json.get("database").getAsString();
        collection = json.get("collection").getAsString();
    }

    @Override
    public void write(JsonObject json) {
        json.addProperty("transactionId", transactionId);
        json.addProperty("query", query);
        json.addProperty("database", database);
        json.addProperty("collection", collection);
    }

    public int getTransactionId() {
        return transactionId;
    }

    public String getQuery() {
        return query;
    }

    public String getDatabase() {
        return database;
    }

    public String getCollection() {
        return collection;
    }
}
