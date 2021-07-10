package net.bluenight.magmadb.server.packet;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import jdk.nashorn.api.scripting.NashornScriptEngineFactory;
import net.bluenight.magmadb.server.ClientConnection;
import net.bluenight.magmadb.server.Server;
import net.bluenight.magmadb.server.packet.clientbound.*;
import net.bluenight.magmadb.server.packet.serverbound.*;
import net.bluenight.magmadb.server.storage.DatabaseStorage;
import net.bluenight.magmadb.server.storage.Profile;
import net.bluenight.magmadb.server.storage.ProfileStorage;
import net.bluenight.magmadb.wrapper.EnumAuthResult;
import net.bluenight.magmadb.wrapper.EnumInteractResult;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * TODO implement encryption
 *  if packetOut == Encryption
 *      connection.setHasEncryption = true
 */
public class PacketHandler {
    NashornScriptEngineFactory factory = new NashornScriptEngineFactory();

    private final Server server;
    private final ProfileStorage profileStorage;
    private final DatabaseStorage databaseStorage;

    public PacketHandler(Server server) {
        this.server = server;
        profileStorage = server.getProfileStorage();
        databaseStorage = server.getDatabaseStorage();
    }

    public void read(ClientConnection connection, String data) throws Exception {
        // TODO implement encryption
        try {
            JsonObject json = (JsonObject) JsonParser.parseString(data);
            int id = json.get("id").getAsInt();
            System.out.println(json);

            Packet packet = (Packet) PacketType.INBOUND[id].newInstance();
            packet.read(json);

            handle(packet, connection);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private synchronized void handle(Packet packet, ClientConnection connection) throws IOException {
        if (packet instanceof PacketInAuthenticate)
            onAuthenticate((PacketInAuthenticate) packet, connection);
        else if (packet instanceof PacketInDatabaseRequest)
            onDatabaseRequest((PacketInDatabaseRequest) packet, connection);
        else if (packet instanceof PacketInCreateDatabase)
            onDatabaseCreate((PacketInCreateDatabase) packet, connection);
        else if (packet instanceof PacketInRemoveDatabase)
            onDatabaseRemove((PacketInRemoveDatabase) packet, connection);
        else if (packet instanceof PacketInCollectionRequest)
            onCollectionRequest((PacketInCollectionRequest) packet, connection);
        else if (packet instanceof PacketInCreateCollection)
            onCollectionCreate((PacketInCreateCollection) packet, connection);
        else if (packet instanceof PacketInFindElement)
            onElementFind((PacketInFindElement) packet, connection);
        else if (packet instanceof PacketInUpdateElement)
            onElementUpdate((PacketInUpdateElement) packet, connection);
    }

    private void onAuthenticate(PacketInAuthenticate packet, ClientConnection connection) throws IOException {
        // TODO rate limit, ip whitelist
        System.out.println("[Server] authenticating " + packet);
        Profile profile = profileStorage.getProfile(packet.getUsername());
        if (profile == null) {
            connection.sendPacket(new PacketOutAuthenticate(EnumAuthResult.UNKNOWN_PROFILE));
            return;
        }
        if (!profile.getPassword().equals(packet.getPassword())) {
            connection.sendPacket(new PacketOutAuthenticate(EnumAuthResult.INVALID_PASSWORD));
            return;
        }
        connection.sendPacket(new PacketOutAuthenticate(EnumAuthResult.SUCCESS));
    }

    private void onDatabaseRequest(PacketInDatabaseRequest packet, ClientConnection connection) throws IOException {
        // TODO check access
        if (databaseStorage.getDatabase(packet.getName()) != null) {
            connection.sendPacket(new PacketOutDatabaseRequest(packet.getName(), EnumInteractResult.FOUND));
            return;
        }
        connection.sendPacket(new PacketOutDatabaseRequest(packet.getName(), EnumInteractResult.NOT_FOUND));
    }

    private void onDatabaseCreate(PacketInCreateDatabase packet, ClientConnection connection) throws IOException {
        // TODO check access
        if (databaseStorage.getDatabase(packet.getName()) != null) {
            connection.sendPacket(new PacketOutCreateDatabase(packet.getName(), EnumInteractResult.DUPLICATE));
            return;
        }
        databaseStorage.createDatabase(packet.getName());
        connection.sendPacket(new PacketOutCreateDatabase(packet.getName(), EnumInteractResult.CREATED));
    }

    private void onDatabaseRemove(PacketInRemoveDatabase packet, ClientConnection connection) throws IOException {
        if (databaseStorage.getDatabase(packet.getName()) == null) {
            connection.sendPacket(new PacketOutRemoveDatabase(packet.getName(), EnumInteractResult.NOT_FOUND));
            return;
        }
        databaseStorage.removeDatabase(packet.getName());
        connection.sendPacket(new PacketOutRemoveDatabase(packet.getName(), EnumInteractResult.DELETED));
    }

    private void onCollectionRequest(PacketInCollectionRequest packet, ClientConnection connection) throws IOException {
        // TODO check access
        if (databaseStorage.getCollection(packet.getDatabase(), packet.getName()) != null) {
            connection.sendPacket(new PacketOutCollectionRequest(packet.getDatabase(), packet.getName(), EnumInteractResult.FOUND));
            return;
        }
        connection.sendPacket(new PacketOutCollectionRequest(packet.getDatabase(), packet.getName(), EnumInteractResult.NOT_FOUND));
    }

    private void onCollectionCreate(PacketInCreateCollection packet, ClientConnection connection) throws IOException {
        if (databaseStorage.getCollection(packet.getDatabase(), packet.getName()) != null) {
            connection.sendPacket(new PacketOutCreateCollection(packet.getDatabase(), packet.getName(), EnumInteractResult.DUPLICATE));
            return;
        }
        databaseStorage.createCollection(packet.getDatabase(), packet.getName());
        connection.sendPacket(new PacketOutCreateCollection(packet.getDatabase(), packet.getName(), EnumInteractResult.CREATED));
    }

    private void onElementFind(PacketInFindElement packet, ClientConnection connection) throws IOException {
        List<JsonObject> elements = databaseStorage.findElements(packet.getDatabase(), packet.getCollection());
        if (elements == null || elements.isEmpty()) {
            connection.sendPacket(new PacketOutFindElement(packet.getTransactionId(), null, EnumInteractResult.NOT_FOUND));
            return;
        }
        ScriptEngine engine = factory.getScriptEngine("--language=es6");
        try {
            writeDefaults(engine);
            engine.eval("function test(e) { return " + packet.getQuery() + " }");
            for (JsonObject element : elements) {
                engine.eval("element = " + element.toString());
                if ((boolean) engine.eval("test(element);")) {
                    connection.sendPacket(new PacketOutFindElement(packet.getTransactionId(), element, EnumInteractResult.FOUND));
                    return;
                }
            }
            connection.sendPacket(new PacketOutFindElement(packet.getTransactionId(), null, EnumInteractResult.NOT_FOUND));
        } catch (ScriptException e) {
            connection.sendPacket(new PacketOutFindElement(packet.getTransactionId(), null, EnumInteractResult.INVALID_SYNTAX));
        }
    }

    // Ya like spaghetti? 🍝 Yummy!
    private void onElementUpdate(PacketInUpdateElement packet, ClientConnection connection) throws IOException {
        JsonObject json = databaseStorage.load();
        for (JsonElement dbElement : json.getAsJsonArray("databases")) {
            JsonObject dbJson = dbElement.getAsJsonObject();
            if (dbJson.get("name").getAsString().equals(packet.getDatabase())) {
                for (JsonElement colElement : dbJson.getAsJsonArray("collections")) {
                    JsonObject colJson = colElement.getAsJsonObject();
                    if (colJson.get("name").getAsString().equals(packet.getCollection())) {
                        JsonArray elements = colJson.getAsJsonArray("elements");
                        ScriptEngine engine = factory.getScriptEngine("--language=es6");
                        try {
                            writeDefaults(engine);
                            engine.eval("function test(e) { return " + packet.getQuery() + " }");
                            for (int i = 0; i < elements.size(); i++) {
                                engine.eval("element = " + elements.get(i).toString());
                                if ((boolean) engine.eval("test(element);")) {
                                    engine.eval("let e = element");
                                    engine.eval(packet.getUpdate() + ";");

                                    String data = (String) engine.eval("JSON.stringify(e);");
                                    System.err.println("OKE " + data);
                                    elements.set(i, JsonParser.parseString(data));

                                    databaseStorage.save(json);
                                    connection.sendPacket(new PacketOutUpdateElement(packet.getTransactionId(), EnumInteractResult.UPDATED));
                                    return;
                                }
                            }
                            connection.sendPacket(new PacketOutUpdateElement(packet.getTransactionId(), EnumInteractResult.NOT_FOUND));
                        } catch (ScriptException e) {
                            connection.sendPacket(new PacketOutFindElement(packet.getTransactionId(), null, EnumInteractResult.INVALID_SYNTAX));
                        }
                        return;
                    }
                }
            }
        }
        connection.sendPacket(new PacketOutUpdateElement(packet.getTransactionId(), EnumInteractResult.NOT_FOUND));
    }

    private void writeDefaults(ScriptEngine engine) throws ScriptException {
        engine.eval("function len(x) { return x.length; }");
        engine.eval("function int(x, radix) { return parseInt(x, radix); }");

        // TODO more utils, etc
    }

    public String write(ClientConnection connection, JsonObject data, Packet packet) {
        //TODO implement encryption
        data.addProperty("id", PacketType.getPacketID(packet));
        packet.write(data);

        return data.toString();
    }
}
