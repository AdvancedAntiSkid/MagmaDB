package net.bluenight.magmadb.old.server;

import net.bluenight.magmadb.old.server.packet.PacketHandler;
import net.bluenight.magmadb.old.util.Logger;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

public class MagmaServer
{
    private final int port;
    private final List<Client> clients;
    private final FileStorage fileStorage;

    private volatile boolean open;
    private ServerSocket socket;
    private PacketHandler packetHandler;

    public MagmaServer(int port)
    {
        this.port = port;
        this.clients = new ArrayList<>();
        fileStorage = new FileStorage();
    }

    public void listen() throws IOException
    {
        socket = new ServerSocket(port);
        fileStorage.setup();
        packetHandler = new PacketHandler(this, socket, true);
        packetHandler.startListener();
        open = true;
        Logger.info("[MagmaDB] Listening on port " + port);
    }

    public void close() throws IOException
    {
        if (socket != null)
        {
            socket.close();
            packetHandler.stopListener();
        }
        open = false;
    }

    public boolean isOpen()
    {
        return open;
    }

    public List<Client> getClients()
    {
        return clients;
    }

    public File getDataFolder()
    {
        return fileStorage.getDataFolder();
    }

    public void setDataFolder(File folder)
    {
        fileStorage.setDataFolder(folder);
    }

    public void setDataFolder(String path)
    {
        setDataFolder(new File(path));
    }
}
