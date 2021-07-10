package net.bluenight.magmadb.server;

import net.bluenight.magmadb.server.packet.PacketHandler;
import net.bluenight.magmadb.server.storage.DatabaseStorage;
import net.bluenight.magmadb.server.storage.ProfileStorage;
import net.bluenight.magmadb.util.Logger;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Server {
    private static final AtomicInteger ID = new AtomicInteger();

    private final MagmaServer server;
    private final PacketHandler packetHandler;
    private final Thread handlerThread;
    private final List<ClientConnection> connections;

    private final File roodDir;
    private final ProfileStorage profileStorage;
    private final DatabaseStorage databaseStorage;

    private ServerSocket socket;

    public Server(MagmaServer server) throws IOException {
        this.server = server;

        roodDir = new File("MagmaDB");
        roodDir.mkdirs();
        profileStorage = new ProfileStorage(new File(roodDir, "profiles.json"));
        databaseStorage = new DatabaseStorage(new File(roodDir, "database.json"));

        packetHandler = new PacketHandler(this);

        handlerThread = new Thread(this::process);
        handlerThread.setName("[MagmaDB Server Thread #" + ID.getAndIncrement() + "]");

        connections = new ArrayList<>();

        Runtime.getRuntime().addShutdownHook(new Thread(server::close));
    }

    public void listen() throws IOException {
        socket = new ServerSocket(server.getPort());
        server.open = true;
        handlerThread.start();
    }

    private void process() {
        while (server.isOpen()) {
            try {
                ClientConnection connection = new ClientConnection(this, packetHandler, socket.accept());
                // TODO eventHandler onConnect
                // if !event->success drop connection

                connections.add(connection);
                connection.open();

                Logger.info("Client connected " + connection.getSocket());

                // connection.sendPacket(new PacketOutAuthenticate(true));

                // System.out.println("server auth sent");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void shutDown() {
        System.out.println("[MagmaDB] Shutting down...");
        // TODO
    }

    public boolean isOpen() {
        return server.isOpen();
    }

    public ProfileStorage getProfileStorage() {
        return profileStorage;
    }

    public DatabaseStorage getDatabaseStorage() {
        return databaseStorage;
    }
}
