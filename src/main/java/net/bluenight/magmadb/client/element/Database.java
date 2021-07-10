package net.bluenight.magmadb.client.element;

import net.bluenight.magmadb.client.Client;
import net.bluenight.magmadb.client.MagmaClient;
import net.bluenight.magmadb.client.exception.*;
import net.bluenight.magmadb.client.packet.Check;
import net.bluenight.magmadb.client.packet.callback.PacketCallbackHandler;
import net.bluenight.magmadb.client.packet.clientbound.CPacketDeleteCollection;
import net.bluenight.magmadb.client.packet.clientbound.CPacketFindCollection;
import net.bluenight.magmadb.client.packet.clientbound.CPacketCreateCollection;
import net.bluenight.magmadb.client.packet.serverbound.SPacketDeleteCollection;
import net.bluenight.magmadb.client.packet.serverbound.SPacketFindCollection;
import net.bluenight.magmadb.client.packet.serverbound.SPacketCreateCollection;
import net.bluenight.magmadb.util.Action;
import net.bluenight.magmadb.wrapper.EnumInteractResult;

import java.util.function.Consumer;

public class Database {
    private String name;
    private final MagmaClient magmaClient;
    private final Client client;
    private final PacketCallbackHandler callbackHandler;

    public Database(String name, MagmaClient client) {
        this.name = name;
        this.magmaClient = client;
        this.client = client.getClient();
        callbackHandler = this.client.getCallbackHandler();
    }

    public Collection getCollection(String name) throws InvalidAccessException, NoSuchCollectionException, MagmaDBException {
        CPacketFindCollection packet = callbackHandler.waitFor(
            Check.testCollectionFind(this.name, name), new SPacketFindCollection(this.name, name));
        EnumInteractResult result = packet.getResult();

        if (result == EnumInteractResult.FOUND) {
            return new Collection(name, this, client);
        }

        if (result == EnumInteractResult.INVALID_ACCESS) {
            throw new InvalidAccessException("Unable to access collection: " + name);
        } else if (result == EnumInteractResult.NOT_FOUND) {
            throw new NoSuchCollectionException("No such collection: " + name);
        } else {
            throw new MagmaDBException("An unknown exception occurred whilst trying to fetch collection: " + name);
        }
    }

    public void getCollection(String name, Consumer<Collection> action)
            throws InvalidAccessException, NoSuchCollectionException, MagmaDBException {
        callbackHandler.waitFor(Check.testCollectionFind(this.name, name), packet -> {
            EnumInteractResult result = packet.getResult();
            if (result == EnumInteractResult.FOUND) {
                action.accept(new Collection(name, this, client));
            }

            if (result == EnumInteractResult.INVALID_ACCESS) {
                throw new InvalidAccessException("Unable to access collection: " + name);
            } else if (result == EnumInteractResult.NOT_FOUND) {
                throw new NoSuchCollectionException("No such collection: " + name);
            } else {
                throw new MagmaDBException("An unknown exception occurred whilst trying to fetch collection: " + name);
            }

        }, new SPacketFindCollection(this.name, name), CPacketFindCollection.class);
    }

    public Collection createCollection(String name) throws InvalidAccessException, CollectionDuplicateException, MagmaDBException {
        CPacketCreateCollection packet = callbackHandler.waitFor(
            Check.testCollectionCreate(this.name, name), new SPacketCreateCollection(this.name, name));
        EnumInteractResult result = packet.getResult();

        if (result == EnumInteractResult.CREATED) {
            return new Collection(name, this, client);
        }

        if (result == EnumInteractResult.INVALID_ACCESS) {
            throw new InvalidAccessException("Unable to access database: " + name);
        } else if (result == EnumInteractResult.DUPLICATE) {
            throw new DatabaseDuplicateException("Duplicate database: " + name);
        } else {
            throw new MagmaDBException("An unknown exception occurred whilst trying to create database: " + name);
        }
    }

    public void createCollection(String name, Consumer<Collection> action)
            throws InvalidAccessException, CollectionDuplicateException, MagmaDBException {
        callbackHandler.waitFor(Check.testCollectionCreate(this.name, name), packet -> {
            EnumInteractResult result = packet.getResult();

            if (result == EnumInteractResult.CREATED) {
                action.accept(new Collection(name, this, client));
            }

            if (result == EnumInteractResult.INVALID_ACCESS) {
                throw new InvalidAccessException("Unable to access database: " + name);
            } else if (result == EnumInteractResult.DUPLICATE) {
                throw new DatabaseDuplicateException("Duplicate database: " + name);
            } else {
                throw new MagmaDBException("An unknown exception occurred whilst trying to create database: " + name);
            }
        }, new SPacketCreateCollection(this.name, name), CPacketCreateCollection.class);
    }
    
    public boolean hasCollection(String name) {
        CPacketFindCollection packet = callbackHandler.waitFor(
            Check.testCollectionFind(this.name, name), new SPacketFindCollection(this.name, name));
        return packet.getResult() != EnumInteractResult.NOT_FOUND;
    }

    public void hasCollection(String name, Consumer<Boolean> action) {
        callbackHandler.waitFor(Check.testCollectionFind(this.name, name), packet ->
            action.accept(packet.getResult() != EnumInteractResult.NOT_FOUND),
            new SPacketFindCollection(this.name, name), CPacketFindCollection.class);
    }

    public void deleteCollection(String name) {
        CPacketDeleteCollection packet = callbackHandler.waitFor(
            Check.testCollectionDelete(this.name, name), new SPacketDeleteCollection(this.name, name));
        EnumInteractResult result = packet.getResult();

        if (result == EnumInteractResult.DELETED) {
            return;
        }

        if (result == EnumInteractResult.INVALID_ACCESS) {
            throw new InvalidAccessException("Unable to access collection: " + name);
        } else if (result == EnumInteractResult.NOT_FOUND) {
            throw new DatabaseDuplicateException("No such collection: " + name);
        } else {
            throw new MagmaDBException("An unknown exception occurred whilst trying to delete collection " + name);
        }
    }

    public void deleteCollection(Collection collection) {
        deleteCollection(collection.getName());
    }

    public void deleteCollection(String name, Action action) {
        callbackHandler.waitFor(Check.testCollectionDelete(this.name, name), packet -> {
            EnumInteractResult result = packet.getResult();

            if (result == EnumInteractResult.DELETED) {
                action.accept();
                return;
            }

            if (result == EnumInteractResult.INVALID_ACCESS) {
                throw new InvalidAccessException("Unable to access collection: " + name);
            } else if (result == EnumInteractResult.NOT_FOUND) {
                throw new DatabaseDuplicateException("No such collection: " + name);
            } else {
                throw new MagmaDBException("An unknown exception occurred whilst trying to delete collection " + name);
            }
        }, new SPacketDeleteCollection(this.name, name), CPacketDeleteCollection.class);
    }

    public void deleteCollection(Collection collection, Action action) {
        deleteCollection(collection.getName(), action);
    }

    public void delete() throws InvalidAccessException, DatabaseDuplicateException, MagmaDBException {
        magmaClient.deleteDatabase(name);
    }

    public void delete(Action action) throws InvalidAccessException, DatabaseDuplicateException, MagmaDBException {
        magmaClient.deleteDatabase(name, action);
    }

    public String getName() {
        return name;
    }
}
