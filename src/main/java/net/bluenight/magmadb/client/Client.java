package net.bluenight.magmadb.client;


import com.google.gson.JsonObject;
import net.bluenight.magmadb.client.packet.Packet;
import net.bluenight.magmadb.client.packet.PacketHandler;
import net.bluenight.magmadb.client.packet.callback.PacketCallbackHandler;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicInteger;

public class Client {
    private static final AtomicInteger ID = new AtomicInteger();

    private final MagmaClient client;
    private final ClientProfile profile;
    private final ClientOptions options;
    private final Thread thread;
    private final PacketCallbackHandler packetCallbackHandler;
    private final PacketHandler packetHandler;

    private Socket socket;

    private volatile ObjectOutputStream streamOut;
    private volatile ObjectInputStream streamIn;

    public Client(MagmaClient client, ClientProfile profile, ClientOptions options) {
        this.client = client;
        this.profile = profile;
        this.options = options;

        packetCallbackHandler = new PacketCallbackHandler(this);
        packetHandler = new PacketHandler(this, packetCallbackHandler);

        thread = new Thread(this::process);
        thread.setName("[MagmaDB Client Thread #" + ID.getAndIncrement() + "]");
    }

    public void listen() throws IOException {
        socket = new Socket(client.getHost(), client.getPort());
        client.open = true;
        thread.start();
        //ObjectOutputStream stream = new ObjectOutputStream(socket.getOutputStream());
        //stream.writeUTF("{\"id\":0,\"lol\":true}");
        //stream.flush();
    }

    private void process() {
        try {
            streamOut = new ObjectOutputStream(socket.getOutputStream());
            streamIn = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        while (client.isOpen()) {
            try {
                String data = streamIn.readUTF();
                System.out.println("CLIENT READ " + data);
                packetHandler.read(data);
            } catch (Exception e) {
                try { close(); }
                catch (IOException ioException) { ioException.printStackTrace(); }
                e.printStackTrace();
            }
        }
    }

    public void sendPacket(Packet packet) throws IOException {
        JsonObject json = new JsonObject();
        write(packetHandler.write(json, packet));
    }

    public void write(String data) throws IOException {
        streamOut.writeUTF(data);
        streamOut.flush();
    }

    public void close() throws IOException {
        thread.stop();
        if (socket != null)
            socket.close();
        if (streamOut != null)
            streamOut.close();
        if (streamIn != null)
            streamIn.close();
    }

    public PacketCallbackHandler getCallbackHandler() {
        return packetCallbackHandler;
    }
}
