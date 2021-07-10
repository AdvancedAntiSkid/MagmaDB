package net.bluenight.magmadb.client;

import net.bluenight.magmadb.client.element.Database;
import net.bluenight.magmadb.client.exception.DatabaseDuplicateException;
import net.bluenight.magmadb.client.exception.InvalidAccessException;
import net.bluenight.magmadb.client.exception.NoSuchDatabaseException;
import net.bluenight.magmadb.client.exception.MagmaDBException;
import net.bluenight.magmadb.client.packet.Check;
import net.bluenight.magmadb.client.packet.callback.PacketCallbackHandler;
import net.bluenight.magmadb.client.packet.clientbound.CPacketCreateDatabase;
import net.bluenight.magmadb.client.packet.clientbound.CPacketDeleteDatabase;
import net.bluenight.magmadb.client.packet.clientbound.CPacketFindDatabase;
import net.bluenight.magmadb.client.packet.serverbound.SPacketCreateDatabase;
import net.bluenight.magmadb.client.packet.serverbound.SPacketDeleteDatabase;
import net.bluenight.magmadb.client.packet.serverbound.SPacketFindDatabase;
import net.bluenight.magmadb.client.packet.serverbound.SPacketLogin;
import net.bluenight.magmadb.util.Action;
import net.bluenight.magmadb.wrapper.EnumInteractResult;

import java.io.IOException;
import java.util.function.Consumer;

public class MagmaClient {
    private String host;
    private int port;

    private ClientProfile profile;
    private ClientOptions options;
    private Client client;
    private PacketCallbackHandler callbackHandler;

    volatile boolean open;

    public MagmaClient(String host, int port, ClientProfile profile, ClientOptions options) {
        bind(host, port);
        this.profile = profile != null ? profile : ClientProfile.DEFAULT;
        this.options = options != null ? options : ClientOptions.DEFAULT;
    }

    public MagmaClient(String host, int port, ClientProfile profile) {
        this(host, port, profile, null);
    }

    public MagmaClient() {
        this("127.0.0.1", 1039, null, null);
    }

    public void bind(String host, int port) {
        this.port = port;
        this.host = host;
        if (host.equals("localhost")) {
            this.host = "127.0.0.1";
        }
    }

    public void listen() throws IOException {
        client = new Client(this, profile, options);
        client.listen();
        client.sendPacket(new SPacketLogin(profile));
        callbackHandler = client.getCallbackHandler();
        System.out.println("client auth sent");
    }

    /**
     * <h1>Get database synchronously</h1>
     * <p>The thread will pause and wait for the server's response</p>
     * @param                     name - the name of the database
     * @return                         - a new instance of the database
     * @throws  InvalidAccessException - the user does not have access to the database
     * @throws NoSuchDatabaseException - the database does not exist
     * @throws MagmaDBException        - unknown exception occurred
     */
    public Database getDatabase(String name) throws InvalidAccessException, NoSuchDatabaseException, MagmaDBException {
        CPacketFindDatabase packet = callbackHandler.waitFor(Check.testDatabaseFind(name), new SPacketFindDatabase(name));
        EnumInteractResult result = packet.getResult();

        if (result == EnumInteractResult.FOUND) {
            return new Database(name, this);
        }

        if (result == EnumInteractResult.INVALID_ACCESS) {
            throw new InvalidAccessException("Unable to access database: " + name);
        } else if (result == EnumInteractResult.NOT_FOUND) {
            throw new NoSuchDatabaseException("No such database: " + name);
        } else {
            throw new MagmaDBException("An unknown exception occurred whilst trying to find database: " + name);
        }
    }

    /**
     * <h1>Get database asynchronously</h1>
     * <p>The action will get called once the server responds</p>
     * @param                     name - the name of the database
     * @param                   action - will get called with the server's response
     * @throws  InvalidAccessException - the user does not have access to the database
     * @throws NoSuchDatabaseException - the database does not exist
     * @throws MagmaDBException        - unknown exception occurred
     */
    public void getDatabase(String name, Consumer<Database> action) throws InvalidAccessException, NoSuchDatabaseException, MagmaDBException {
        callbackHandler.waitFor(Check.testDatabaseFind(name), packet -> {
            EnumInteractResult result = packet.getResult();

            if (result == EnumInteractResult.FOUND) {
                action.accept(new Database(name, this));
            }

            if (result == EnumInteractResult.INVALID_ACCESS) {
                throw new InvalidAccessException("Unable to access database: " + name);
            } else if (result == EnumInteractResult.NOT_FOUND) {
                throw new NoSuchDatabaseException("No such database: " + name);
            } else {
                throw new MagmaDBException("An unknown exception occurred whilst trying to find database: " + name);
            }
        }, new SPacketFindDatabase(name), CPacketFindDatabase.class);
    }

    public Database createDatabase(String name) throws InvalidAccessException, DatabaseDuplicateException, MagmaDBException {
        CPacketCreateDatabase packet = callbackHandler.waitFor(Check.testDatabaseCreate(name), new SPacketCreateDatabase(name));
        EnumInteractResult result = packet.getResult();

        if (result == EnumInteractResult.CREATED) {
            return new Database(name, this);
        }

        if (result == EnumInteractResult.INVALID_ACCESS) {
            throw new InvalidAccessException("Unable to access database: " + name);
        } else if (result == EnumInteractResult.DUPLICATE) {
            throw new DatabaseDuplicateException("Duplicate database: " + name);
        } else {
            throw new MagmaDBException("An unknown exception occurred whilst trying to create database: " + name);
        }
    }

    public void createDatabase(String name, Consumer<Database> action) throws InvalidAccessException, DatabaseDuplicateException, MagmaDBException {
        callbackHandler.waitFor(Check.testDatabaseFind(name), packet -> {
            EnumInteractResult result = packet.getResult();

            if (result == EnumInteractResult.CREATED) {
                action.accept(new Database(name, this));
            }

            if (result == EnumInteractResult.INVALID_ACCESS) {
                throw new InvalidAccessException("Unable to access database: " + name);
            } else if (result == EnumInteractResult.DUPLICATE) {
                throw new DatabaseDuplicateException("Duplicate database: " + name);
            } else {
                throw new MagmaDBException("An unknown exception occurred whilst trying to create database: " + name);
            }
        }, new SPacketCreateDatabase(name), CPacketCreateDatabase.class);
    }

    @Deprecated
    public void deleteDatabase(String name) throws InvalidAccessException, DatabaseDuplicateException, MagmaDBException {
        CPacketDeleteDatabase packet = callbackHandler.waitFor(Check.testDatabaseDelete(name), new SPacketDeleteDatabase(name));
        EnumInteractResult result = packet.getResult();

        if (result == EnumInteractResult.DELETED) {
            return;
        }

        if (result == EnumInteractResult.INVALID_ACCESS) {
            throw new InvalidAccessException("Unable to access database: " + name);
        } else if (result == EnumInteractResult.NOT_FOUND) {
            throw new DatabaseDuplicateException("No such database: " + name);
        } else {
            throw new MagmaDBException("An unknown exception occurred whilst trying to delete database: " + name);
        }
    }

    public void deleteDatabase(Database database) throws InvalidAccessException, DatabaseDuplicateException, MagmaDBException {
        deleteDatabase(database.getName());
    }

    public void deleteDatabase(String name, Action action) throws InvalidAccessException, DatabaseDuplicateException, MagmaDBException {
        callbackHandler.waitFor(Check.testDatabaseDelete(name), packet -> {
            EnumInteractResult result = packet.getResult();
            if (result == EnumInteractResult.DELETED) {
                action.accept();
                return;
            }

            if (result == EnumInteractResult.INVALID_ACCESS) {
                throw new InvalidAccessException("Unable to access database: " + name);
            } else if (result == EnumInteractResult.NOT_FOUND) {
                throw new DatabaseDuplicateException("No such database: " + name);
            } else {
                throw new MagmaDBException("An unknown exception occurred whilst trying to delete database: " + name);
            }
        }, new SPacketDeleteDatabase(name), CPacketDeleteDatabase.class);
    }

    public void deleteDatabase(Database database, Action action) throws InvalidAccessException, DatabaseDuplicateException, MagmaDBException {
        deleteDatabase(database.getName(), action);
    }

    public boolean hasDatabase(String name) {
        CPacketFindDatabase packet = callbackHandler.waitFor(Check.testDatabaseFind(name), new SPacketFindDatabase(name));
        return packet.getResult() != EnumInteractResult.NOT_FOUND;
    }

    public void hasDatabase(String name, Consumer<Boolean> action) {
        callbackHandler.waitFor(Check.testDatabaseFind(name), packet ->
            action.accept(packet.getResult() != EnumInteractResult.NOT_FOUND),
            new SPacketFindDatabase(name), CPacketFindDatabase.class);
    }

    // TODO @Deprecated createCollection(String database, String name) {

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public ClientProfile getProfile() {
        return profile;
    }

    public void setProfile(ClientProfile profile) {
        this.profile = profile;
    }

    public ClientOptions getOptions() {
        return options;
    }

    public void setOptions(ClientOptions options) {
        this.options = options;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public Client getClient() {
        return client;
    }
}
