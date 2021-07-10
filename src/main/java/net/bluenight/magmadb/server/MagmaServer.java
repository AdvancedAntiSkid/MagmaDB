package net.bluenight.magmadb.server;

import java.io.IOException;

public class MagmaServer {
    private int port;
    private Server server;

    volatile boolean open;

    public MagmaServer(int port) {
        bind(port);
    }

    public MagmaServer() {
        this(1039);
    }

    public void listen() throws IOException {
        server = new Server(this);
        server.listen();
    }

    public void bind(int port) {
        this.port = port;
    }

    public void close() {
        if (open) {
            // TODO close logic
            open = false;
        }
    }

    public int getPort() {
        return port;
    }

    public Server getServer() {
        return server;
    }

    public boolean isOpen() {
        return open;
    }
}
