package net.bluenight.magmadb.server;

import com.google.gson.JsonObject;
import net.bluenight.magmadb.server.packet.Packet;
import net.bluenight.magmadb.server.packet.PacketHandler;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicInteger;

public class ClientConnection
{
    private static final AtomicInteger ID = new AtomicInteger();

    private final Server server;
    private final PacketHandler packetHandler;

    private final Socket socket;
    private final Thread thread;

    private ObjectOutputStream streamOut;
    private ObjectInputStream streamIn;

    private volatile boolean encryption;
    private volatile boolean authenticated;

    public ClientConnection(Server server, PacketHandler packetHandler, Socket socket)
    {
        this.server = server;
        this.packetHandler = packetHandler;
        this.socket = socket;
        thread = new Thread(this::process);
        thread.setName("[MagmaDB Server Client Thread #" + ID.getAndIncrement() + "]");
    }

    private void process()
    {
        try
        {
            streamOut = new ObjectOutputStream(socket.getOutputStream());
            streamIn = new ObjectInputStream(socket.getInputStream());
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        while (server.isOpen())
        {
            try
            {
                String data = streamIn.readUTF();
                packetHandler.read(this, data);
            }
            catch (Exception e)
            {
                try { close(); }
                catch (Exception ex) { ex.printStackTrace(); }
                e.printStackTrace();
            }
        }
    }

    public void open()
    {
        thread.start();
    }

    public void sendPacket(Packet packet) throws IOException
    {
        JsonObject json = new JsonObject();
        write(packetHandler.write(this, json, packet));
    }

    public void write(String data) throws IOException
    {
        streamOut.writeUTF(data);
        streamOut.flush();
    }

    public void close() throws IOException
    {
        thread.stop();
        if (socket != null)
            socket.close();
        if (streamOut != null)
            streamOut.close();
        if (streamIn != null)
            streamIn.close();
    }

    public Socket getSocket()
    {
        return socket;
    }

    public boolean hasEncryption()
    {
        return encryption;
    }

    public void setEncryption(boolean encryption)
    {
        this.encryption = encryption;
    }

    public boolean isAuthenticated()
    {
        return authenticated;
    }

    public void setAuthenticated(boolean authenticated)
    {
        this.authenticated = authenticated;
    }
}
