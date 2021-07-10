package net.bluenight.magmadb.old.server.packet;

import net.bluenight.magmadb.old.packet.clientbound.PacketOutAuthenticate;
import net.bluenight.magmadb.old.server.Client;
import net.bluenight.magmadb.old.server.MagmaServer;
import net.bluenight.magmadb.old.util.Pair;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PacketListener
{
    private final PacketHandler packetHandler;
    private final MagmaServer server;
    private final ServerSocket socket;
    private final boolean async;
    private final List<PacketAdapter> adaptersInbound;
    private final List<PacketAdapter> adaptersOutbound;

    private boolean running;
    private Thread listenerThread;
    private Thread asyncCheckThread;
    private List<Pair<Pair<Object, Client>, Boolean>> asyncQueuedPackets;

    public PacketListener(PacketHandler packetHandler, MagmaServer server, ServerSocket socket, boolean async)
    {
        this.packetHandler = packetHandler;
        this.server = server;
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

    public void enable()
    {
        running = true;
        listenerThread.start();
        if (async)
        {
            asyncCheckThread.start();
        }
    }

    public void disable()
    {
        running = false;
        listenerThread.stop();
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
    public boolean processIn(Object packet, Client client)
    {
        System.out.println("[SERVER] Packet in " + packet);
        if(!running)
            return true;
        if(async)
        {
            addToAsyncQueue(packet, client, true);
            return true;
        }
        return dispatchInbound(packet, client);
    }

    boolean processOut(Object packet, Client client)
    {
        if(!running)
            return true;
        if(async)
        {
            addToAsyncQueue(packet, client, false);
            return true;
        }
        return dispatchOutbound(packet, client);
    }

    private void addToAsyncQueue(Object packet, Client client, boolean inbound)
    {
        Pair<Object, Client> playerAndClient = new Pair<>(packet, client);
        Pair<Pair<Object, Client>, Boolean> pair = new Pair<>(playerAndClient, inbound);
        asyncQueuedPackets.add(pair);

        synchronized (asyncCheckThread)
        {
            asyncCheckThread.notify();
        }
    }

    private boolean dispatchInbound(Object packet, Client client)
    {
        try
        {
            for(PacketAdapter adapter : adaptersInbound)
            {
                adapter.run(packet, client);
            }

            if (!packetHandler.processIn(packet, client))
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

    private boolean dispatchOutbound(Object packet, Client client)
    {
        try
        {
            for(PacketAdapter adapter : adaptersOutbound)
            {
                adapter.run(packet, client);
            }

            if (!packetHandler.processOut(packet, client))
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
            while (server.isOpen())
            {
                try
                {
                    Client client = new Client(server, this, socket.accept());
                    server.getClients().add(client);
                    client.open();

                    client.write(new PacketOutAuthenticate(true));
                }
                catch (IOException e)
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
                List<Pair<Pair<Object, Client>, Boolean>> packetBatch = new ArrayList<>(asyncQueuedPackets);
                for (Pair<Pair<Object, Client>, Boolean> pair : packetBatch)
                {
                    Pair<Object, Client> packetAndClient = pair.getKey();

                    Object packet = packetAndClient.getKey();
                    Client client = packetAndClient.getValue();
                    boolean inbound = pair.getValue();

                    if(inbound)
                    {
                        dispatchInbound(packet, client);
                    }
                    else
                    {
                        dispatchOutbound(packet, client);
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

        asyncCheckThread.setName("MagmaDB Server Async Check Thread");
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
}
