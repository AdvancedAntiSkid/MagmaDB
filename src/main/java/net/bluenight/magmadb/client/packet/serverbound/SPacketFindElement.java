package net.bluenight.magmadb.client.packet.serverbound;

import com.google.gson.JsonObject;
import net.bluenight.magmadb.client.packet.Packet;

public class SPacketFindElement implements Packet {
    private int transactionId;
    private String query;
    private String database;
    private String collection;

    public SPacketFindElement(int transactionId, String query, String database, String collection) {
        this.transactionId = transactionId;
        this.query = query;
        this.database = database;
        this.collection = collection;
    }

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

    @Override
    public String toString() {
        return "SPacketFindElement{"
            + "transactionId=" + transactionId
            + ", query='" + query + '\''
            + ", database='" + database + '\''
            + ", collection='" + collection + '\''
            + '}';
    }
}
