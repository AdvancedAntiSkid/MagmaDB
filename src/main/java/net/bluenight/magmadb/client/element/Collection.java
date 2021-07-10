package net.bluenight.magmadb.client.element;

import net.bluenight.magmadb.client.Client;
import net.bluenight.magmadb.client.exception.InvalidAccessException;
import net.bluenight.magmadb.client.exception.InvalidSyntaxException;
import net.bluenight.magmadb.client.exception.MagmaDBException;
import net.bluenight.magmadb.client.packet.Check;
import net.bluenight.magmadb.client.packet.callback.PacketCallbackHandler;
import net.bluenight.magmadb.client.packet.clientbound.CPacketFindElement;
import net.bluenight.magmadb.client.packet.clientbound.CPacketUpdateElement;
import net.bluenight.magmadb.client.packet.serverbound.SPacketFindElement;
import net.bluenight.magmadb.client.packet.serverbound.SPacketUpdateElement;
import net.bluenight.magmadb.util.Action;
import net.bluenight.magmadb.wrapper.EnumInteractResult;

import java.util.function.Consumer;

public class Collection {
    private final String name;
    private final Database database;
    private final Client client;
    private final PacketCallbackHandler callbackHandler;

    public Collection(String name, Database database, Client client) {
        this.name = name;
        this.database = database;
        this.client = client;
        callbackHandler = client.getCallbackHandler();
    }

    public Element findOne(String query) {
        int id = (int) (Math.random() * 10000);
        CPacketFindElement packet = callbackHandler.waitFor(Check.testElementFind(id), new SPacketFindElement(id, query, database.getName(), name));
        EnumInteractResult result = packet.getResult();

        if (result == EnumInteractResult.FOUND) {
            return Element.fromJson(this, packet.getData());
        } else if (result == EnumInteractResult.NOT_FOUND) {
            return null;
        } else if (result == EnumInteractResult.INVALID_ACCESS) {
            throw new InvalidAccessException("Unable to access element " + database.getName() + ":" + name + ":'" + query + "'");
        } else if (result == EnumInteractResult.INVALID_SYNTAX) {
            throw new InvalidSyntaxException("Invalid syntax in query '" + query + "'");
        } else {
            throw new MagmaDBException("An unknown exception occurred whilst trying to find element: " + database.getName() + ":" + name + ":'" + query + "'");
        }
    }

    public void findOne(String query, Consumer<Element> action) {
        int id = (int) (Math.random() * 10000);
        callbackHandler.waitFor(Check.testElementFind(id), packet -> {
            EnumInteractResult result = packet.getResult();

            if (result == EnumInteractResult.FOUND) {
                action.accept(Element.fromJson(this, packet.getData()));
            } else if (result == EnumInteractResult.NOT_FOUND) {
                action.accept(null);
            } else if (result == EnumInteractResult.INVALID_ACCESS) {
                throw new InvalidAccessException("Unable to access element " + database.getName() + ":" + name + ":'" + query + "'");
            } else if (result == EnumInteractResult.INVALID_SYNTAX) {
                throw new InvalidSyntaxException("Invalid syntax in query'" + query + "'");
            } else {
                throw new MagmaDBException("An unknown exception occurred whilst trying to find element: " + database.getName() + ":" + name + ":'" + query + "'");
            }
        }, new SPacketFindElement(id, query, database.getName(), name), CPacketFindElement.class);
    }

    public boolean updateOne(String query, String update) {
        int id = (int) (Math.random() * 10000);
        CPacketUpdateElement packet = callbackHandler.waitFor(Check.testElementUpdate(id),
            new SPacketUpdateElement(id, database.getName(), name, query, update));
        EnumInteractResult result = packet.getResult();

        if (result == EnumInteractResult.UPDATED) {
            return true;
        } else if (result == EnumInteractResult.NOT_FOUND) {
            return false;
        } else if (result == EnumInteractResult.INVALID_ACCESS) {
            throw new InvalidAccessException("Unable to access element " + database.getName() + ":" + name + ":'" + query + "'");
        } else if (result == EnumInteractResult.INVALID_SYNTAX) {
            throw new InvalidSyntaxException("Invalid syntax in query'" + query + "'");
        } else {
            throw new MagmaDBException("An unknown exception occurred whilst trying to find element: " + database.getName() + ":" + name + ":'" + query + "'");
        }
    }

    public void updateOne(String query, String update, Consumer<Boolean> action) {
        int id = (int) (Math.random() * 10000);
        callbackHandler.waitFor(Check.testElementUpdate(id), packet -> {
            EnumInteractResult result = packet.getResult();

            if (result == EnumInteractResult.UPDATED) {
                action.accept(true);
            } else if (result == EnumInteractResult.NOT_FOUND) {
                action.accept(false);
            } else if (result == EnumInteractResult.INVALID_ACCESS) {
                throw new InvalidAccessException("Unable to access element " + database.getName() + ":" + name + ":'" + query + "'");
            } else if (result == EnumInteractResult.INVALID_SYNTAX) {
                throw new InvalidSyntaxException("Invalid syntax in query'" + query + "'");
            } else {
                throw new MagmaDBException("An unknown exception occurred whilst trying to find element: " + database.getName() + ":" + name + ":'" + query + "'");
            }
        }, new SPacketUpdateElement(id, database.getName(), name, query, update), CPacketUpdateElement.class);
    }

    public void delete() {
        database.deleteCollection(this);
    }

    public void delete(Action action) {
        database.deleteCollection(name, action);
    }

    public String getName() {
        return name;
    }

    public Database getDatabase() {
        return database;
    }

    @Override
    public String toString() {
        return "Collection{"
            + "name='" + name + '\''
            + '}';
    }
}
