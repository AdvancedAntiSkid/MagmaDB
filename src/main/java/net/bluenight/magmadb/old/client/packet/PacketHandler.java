package net.bluenight.magmadb.old.client.packet;

import net.bluenight.magmadb.old.client.Connection;
import net.bluenight.magmadb.old.client.event.Event;
import net.bluenight.magmadb.old.client.event.EventListener;

import java.io.IOException;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class PacketHandler
{
    private final Connection connection;
    private final Socket socket;
    private final boolean async;
    private final List<EventListener> eventListeners;

    private PacketListener packetListener;

    public PacketHandler(Connection connection, Socket socket, boolean async)
    {
        this.connection = connection;
        this.socket = socket;
        this.async = async;
        eventListeners = new CopyOnWriteArrayList<>();
    }

    /**
     * These packets will be converted into MagmaDB Events
     */
    public boolean processIn(Object packet)
    {
        Event event = PacketConverter.packetInboundToEvent(packet);
        if (event == null)
            return true;

        if (!event.preProcess())
            return false;

        for (EventListener listener : eventListeners)
            listener.onEvent(event);

        // dispatchEvent(event);

        event.postProcess();

        return !event.isCancelled();
    }

    /**
     * These packets will be converted into MagmaDB Events
     */
    public boolean processOut(Object packet)
    {
        Event event = PacketConverter.processOutboundToEvent(packet);
        if (event == null)
            return true;

        for (EventListener listener : eventListeners)
            listener.onEvent(event);

        // dispatchEvent(event);

        return !event.isCancelled();
    }

    public void startListener()
    {
        packetListener = new PacketListener(this, connection, socket, async);
        try
        {
            packetListener.enable();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void stopListener()
    {
        if(packetListener != null)
        {
            try
            {
                packetListener.disable();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    public PacketListener getPacketListener()
    {
        return packetListener;
    }

    public void addPacketAdapterInbound(PacketAdapter adapter)
    {
        packetListener.addAdapterInbound(adapter);
    }

    public void removePacketAdapterInbound(PacketAdapter adapter)
    {
        packetListener.removeAdapterInbound(adapter);
    }

    public void addPacketAdapterOutbound(PacketAdapter adapter)
    {
        packetListener.addAdapterOutbound(adapter);
    }

    public void removePacketAdapterOutbound(PacketAdapter adapter)
    {
        packetListener.removeAdapterOutbound(adapter);
    }

    public List<EventListener> getEventListeners()
    {
        return eventListeners;
    }
}
