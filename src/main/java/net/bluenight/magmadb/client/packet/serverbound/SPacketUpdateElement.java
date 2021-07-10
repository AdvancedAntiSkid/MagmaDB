package net.bluenight.magmadb.client.packet.serverbound;

import com.google.gson.JsonObject;
import net.bluenight.magmadb.client.packet.Packet;

public class SPacketUpdateElement implements Packet {
    private int transactionId;
    private String database;
    private String collection;
    private String query;
    private String update;

    public SPacketUpdateElement(int transactionId, String database, String collection, String query, String update) {
        this.transactionId = transactionId;
        this.database = database;
        this.collection = collection;
        this.query = query;
        this.update = update;
    }

    @Override
    public void read(JsonObject json) {
        transactionId = json.get("transactionId").getAsInt();
        database = json.get("database").getAsString();
        collection = json.get("collection").getAsString();
        query = json.get("query").getAsString();
        update = json.get("update").getAsString();
    }

    @Override
    public void write(JsonObject json) {
        json.addProperty("transactionId", transactionId);
        json.addProperty("database", database);
        json.addProperty("collection", collection);
        json.addProperty("query", query);
        json.addProperty("update", update);
    }

    public int getTransactionId() {
        return transactionId;
    }

    public String getDatabase() {
        return database;
    }

    public String getCollection() {
        return collection;
    }

    public String getQuery() {
        return query;
    }

    public String getUpdate() {
        return update;
    }
}
