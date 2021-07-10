package net.bluenight.magmadb.old.client.packet;

import net.bluenight.magmadb.old.client.Connection;
import net.bluenight.magmadb.old.util.Pair;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PacketListener
{
    private final PacketHandler packetHandler;
    private final Connection connection;
    private final Socket socket;
    private final boolean async;
    private final List<PacketAdapter> adaptersInbound;
    private final List<PacketAdapter> adaptersOutbound;

    private boolean running;
    private Thread listenerThread;
    private Thread asyncCheckThread;
    private ObjectOutputStream streamOut;
    private ObjectInputStream streamIn;
    private List<Pair<Object, Boolean>> asyncQueuedPackets;

    public PacketListener(PacketHandler packetHandler, Connection connection, Socket socket, boolean async)
    {
        this.packetHandler = packetHandler;
        this.connection = connection;
        this.socket = socket;
        this.async = async;
        adaptersInbound = new ArrayList<>();
        adaptersOutbound = new ArrayList<>();
        setupListener();
        if (async)
        {
            prepareAsync();
        }
    }

    public void enable() throws IOException
    {
        running = true;
        listenerThread.start();
        streamOut = new ObjectOutputStream(socket.getOutputStream());
        streamIn = new ObjectInputStream(socket.getInputStream());
        if (async)
        {
            asyncCheckThread.start();
        }
    }

    public void disable() throws IOException
    {
        running = false;
        listenerThread.stop();
        if (socket != null)
            socket.close();
        if (streamOut != null)
            streamOut.close();
        if (streamIn != null)
            streamIn.close();
        if (async)
        {
            synchronized (asyncCheckThread)
            {
                asyncCheckThread.notify();
            }
        }
    }

    /**
     * Returns false if not async and packet fails checks
     */
    public boolean processIn(Object packet)
    {
        System.out.println("[CLIENT Packet in " + packet);
        if (!running)
            return true;
        if (async)
        {
            addToAsyncQueue(packet, true);
            return true;
        }
        return dispatchInbound(packet);
    }

    boolean processOut(Object packet)
    {
        if (!running)
            return true;
        if (async)
        {
            addToAsyncQueue(packet, false);
            return true;
        }
        return dispatchOutbound(packet);
    }

    private void addToAsyncQueue(Object packet, boolean inbound)
    {
        Pair<Object, Boolean> pair = new Pair<>(packet, inbound);
        asyncQueuedPackets.add(pair);

        synchronized (asyncCheckThread)
        {
            asyncCheckThread.notify();
        }
    }

    private boolean dispatchInbound(Object packet)
    {
        try
        {
            for(PacketAdapter adapter : adaptersInbound)
            {
                adapter.run(packet);
            }

            if (!packetHandler.processIn(packet))
                return false;
        }
        catch (Exception e)
        {
            // printPacketErrorInformation(packet, client);
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private boolean dispatchOutbound(Object packet)
    {
        try
        {
            for(PacketAdapter adapter : adaptersOutbound)
            {
                adapter.run(packet);
            }

            if (!packetHandler.processOut(packet))
                return false;
        }
        catch (Exception e)
        {
            // printPacketErrorInformation(packet, client);
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private void setupListener()
    {
        listenerThread = new Thread(() ->
        {
            while (connection.isOpen())
            {
                try
                {
                    processIn(streamIn.readObject());
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
    }

    private void prepareAsync()
    {
        asyncQueuedPackets = Collections.synchronizedList(new ArrayList<>());

        asyncCheckThread = new Thread(() ->
        {
            while (running)
            {
                // Copy contents from queue to batch for processing
                List<Pair<Object, Boolean>> packetBatch = new ArrayList<>(asyncQueuedPackets);
                for (Pair<Object, Boolean> pair : packetBatch)
                {
                    Object packet = pair.getKey();
                    boolean inbound = pair.getValue();

                    if(inbound)
                    {
                        dispatchInbound(packet);
                    }
                    else
                    {
                        dispatchOutbound(packet);
                    }
                }

                // Remove processed contents from queue.
                // Continue loop if queue isn't empty.
                // Otherwise, wait until notification from check thread.
                synchronized (asyncQueuedPackets)
                {
                    asyncQueuedPackets.subList(0, packetBatch.size()).clear();
                    if(asyncQueuedPackets.size() > 0)
                        continue;
                }

                try
                {
                    synchronized (asyncCheckThread)
                    {
                        asyncCheckThread.wait();
                    }
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
        });

        asyncCheckThread.setName("MagmaDB Client Async Check Thread");
    }

    public void write(Object packet) throws IOException
    {
        streamOut.writeObject(packet);
        streamOut.flush();
    }

    public void addAdapterInbound(PacketAdapter adapter)
    {
        adaptersInbound.add(adapter);
    }

    public void removeAdapterInbound(PacketAdapter adapter)
    {
        adaptersInbound.remove(adapter);
    }

    public void addAdapterOutbound(PacketAdapter adapter)
    {
        adaptersOutbound.add(adapter);
    }

    public void removeAdapterOutbound(PacketAdapter adapter)
    {
        adaptersOutbound.remove(adapter);
    }

    public boolean isAsync()
    {
        return async;
    }

    public boolean isRunning()
    {
        return running;
    }

    @Deprecated
    public ObjectOutputStream getStreamOut()
    {
        return streamOut;
    }
}
