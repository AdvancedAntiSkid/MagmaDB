package net.bluenight.magmadb.client.packet.callback;

import net.bluenight.magmadb.client.Client;
import net.bluenight.magmadb.client.exception.MagmaDBException;
import net.bluenight.magmadb.client.packet.Check;
import net.bluenight.magmadb.client.packet.Packet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

public class PacketCallbackHandler {
    private final List<PacketCallback<?>> callbackQueue;
    private final Client client;

    public PacketCallbackHandler(Client client) {
        callbackQueue = Collections.synchronizedList(new ArrayList<>());
        this.client = client;
    }

    public <TReceive extends Packet, TSend extends Packet> PacketCallback<TReceive> waitFor(Check check, Consumer<TReceive> action, TSend packetToSend) {
        PacketCallback<TReceive> callback = new PacketCallback<>(check, action);
        callbackQueue.add(callback);
        try {
            client.sendPacket(packetToSend);
        }
        catch (IOException e) {
            throw new MagmaDBException("En error occurred whilst sending packet", e);
        }
        return callback;
    }

    public <TReceive extends Packet, TSend extends Packet> PacketCallback<TReceive> waitFor(Check check, Consumer<TReceive> action, TSend packetToSend, Class<TReceive> type) {
        PacketCallback<TReceive> callback = new PacketCallback<>(check, action);
        callbackQueue.add(callback);
        try {
            client.sendPacket(packetToSend);
        }
        catch (IOException e) {
            throw new MagmaDBException("En error occurred whilst sending packet", e);
        }
        return callback;
    }

    public <TReceive extends Packet, TSend extends Packet> TReceive waitFor(Check check, TSend packetToSend) {
        PacketCallback<TReceive> callback = waitFor(check, packet -> {}, packetToSend);
        while (!callback.isDone()) {
            // pause thread
        }
        return callback.getPacket();
    }

    public void handle(Packet packet) {
        Iterator<PacketCallback<?>> iterator = callbackQueue.iterator();
        while (iterator.hasNext()) {
            PacketCallback<?> callback = iterator.next();
            if (callback.getCheck().test(packet)) {
                callback.call(packet);
                iterator.remove();
                break;
            }
        }
    }
}
