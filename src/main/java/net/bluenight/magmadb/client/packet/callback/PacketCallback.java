package net.bluenight.magmadb.client.packet.callback;

import net.bluenight.magmadb.client.packet.Check;
import net.bluenight.magmadb.client.packet.Packet;

import java.util.function.Consumer;

public class PacketCallback<T extends Packet> {
    private final Check check;
    private final Consumer<T> action;
    private final long timestamp;

    private volatile boolean done;
    private volatile T packet;

    public PacketCallback(Check check, Consumer<T> action) {
        this.check = check;
        this.action = action;
        timestamp = System.currentTimeMillis();
    }

    @SuppressWarnings("unchecked")
    public void call(Packet packet) {
        action.accept(this.packet = (T) packet);
        done = true;
    }

    public Check getCheck() {
        return check;
    }

    public Consumer<T> getAction() {
        return action;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public boolean isDone() {
        return done;
    }

    public T getPacket() {
        return packet;
    }
}
