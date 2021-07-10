package net.bluenight.magmadb.old.client;

import net.bluenight.magmadb.old.client.packet.PacketHandler;
import net.bluenight.magmadb.old.packet.serverbound.PacketInAuthenticate;

import java.io.IOException;
import java.net.Socket;

public class ClientConnection implements Connection
{
    private final MagmaClient client;
    private final ClientProfile profile;
    private final ClientOptions options;

    private volatile boolean open;
    private Socket socket;
    private PacketHandler packetHandler;
    private String data;

    public ClientConnection(MagmaClient client, ClientProfile profile, ClientOptions options)
    {
        this.client = client;
        this.profile = profile;
        this.options = options;
    }

    @Override
    public void listen(boolean async) throws IOException
    {
        socket = new Socket(client.getHost(), client.getPort());
        packetHandler = new PacketHandler(this, socket, async);
        packetHandler.startListener();
        open = true;
    }

    @Override
    public void listen() throws IOException
    {
        listen(true);
        write(new PacketInAuthenticate(profile.getUsername(), profile.getPassword(), profile.getDatabase(), data));
    }

    @Override
    public void close() throws IOException
    {
        if (socket != null)
        {
            socket.close();
            packetHandler.stopListener();
        }
        open = false;
    }

    @Override
    public boolean isOpen()
    {
        return open;
    }

    private void write(Object packet)
    {
        try
        {
            packetHandler.getPacketListener().write(packet);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    @Deprecated
    public PacketHandler getPacketHandler()
    {
        return packetHandler;
    }

    public String getData()
    {
        return data;
    }

    public void setData(String data)
    {
        this.data = data;
    }
}
