package net.bluenight.magmadb.old.server.packet;

import net.bluenight.magmadb.old.server.Client;
import net.bluenight.magmadb.old.server.MagmaServer;
import net.bluenight.magmadb.old.server.event.*;

import java.net.ServerSocket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class PacketHandler
{
    private final MagmaServer server;
    private final boolean async;
    private final ServerSocket socket;
    private final List<EventListener> eventListeners;
    private final List<ListenerAdapter> listenerAdapters;

    private PacketListener packetListener;

    public PacketHandler(MagmaServer server, ServerSocket socket, boolean async)
    {
        this.server = server;
        this.socket = socket;
        this.async = async;
        eventListeners = new CopyOnWriteArrayList<>();
        listenerAdapters = new CopyOnWriteArrayList<>();
        listenerAdapters.add(new DefaultListenerAdapter());
    }

    /**
     * These packets will be converted into MagmaDB Events
     */
    public boolean processIn(Object packet, Client client)
    {
        Event event = PacketConverter.packetInboundToEvent(packet, client);
        if (event == null)
            return true;

        if (!event.preProcess())
            return false;

        for (EventListener listener : eventListeners)
            listener.onEvent(event);

        dispatchEvent(event);

        event.postProcess();

        return !event.isCancelled();
    }

    /**
     * These packets will be converted into MagmaDB Events
     */
    public boolean processOut(Object packet, Client client)
    {
        Event event = PacketConverter.processOutboundToEvent(packet, client);
        if (event == null)
            return true;

        for (EventListener listener : eventListeners)
            listener.onEvent(event);

        dispatchEvent(event);

        return !event.isCancelled();
    }

    private void dispatchEvent(Event event)
    {
        for (ListenerAdapter adapter : listenerAdapters)
        {
            if (event instanceof ClientLoginEvent)
            {
                adapter.onClientLogin((ClientLoginEvent) event);
            }
        }
    }

    public void startListener()
    {
        packetListener = new PacketListener(this, server, socket, async);
        packetListener.enable();
    }

    public void stopListener()
    {
        if(packetListener != null)
            packetListener.disable();
    }

    @Deprecated
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
