package net.bluenight.magmadb.old.server;

import net.bluenight.magmadb.old.server.packet.PacketListener;
import net.bluenight.magmadb.old.util.Logger;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Client
{
    private final MagmaServer server;
    private final Socket socket;
    private final PacketListener packetListener;

    private Thread thread;
    private ObjectOutputStream streamOut;
    private ObjectInputStream streamIn;

    private volatile boolean authenticated;

    public Client(MagmaServer server, PacketListener packetListener, Socket socket)
    {
        this.server = server;
        this.packetListener = packetListener;
        this.socket = socket;
        Logger.info("Client joined " + socket);
        setupListenThread();
    }

    public void open() throws IOException
    {
        streamOut = new ObjectOutputStream(socket.getOutputStream());
        streamIn = new ObjectInputStream(socket.getInputStream());
        thread.start();
    }

    private void setupListenThread()
    {
        thread = new Thread(() ->
        {
            while (server.isOpen())
            {
                try
                {
                    packetListener.processIn(streamIn.readObject(), this);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    try
                    {
                        close();
                    }
                    catch (IOException ex)
                    {
                        ex.printStackTrace();
                    }
                }
            }
        });

        thread.setName("MagmaDB Client #" + socket.getPort() + " Thread");
    }

    public void write(Object packet) throws IOException
    {
        streamOut.writeObject(packet);
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

    public boolean isAuthenticated()
    {
        return authenticated;
    }

    public void setAuthenticated(boolean authenticated)
    {
        this.authenticated = authenticated;
    }
}
