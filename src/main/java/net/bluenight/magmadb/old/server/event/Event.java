package net.bluenight.magmadb.old.server.event;

import net.bluenight.magmadb.old.server.Client;

public abstract class Event
{
    protected final Client client;
    protected boolean cancelled;

    public Event(Client client)
    {
        this.client = client;
    }

    /**
     * To be implemented by other Events
     */
    public boolean preProcess()
    {
        return true;
    }

    /**
     * To be implemented by other Events
     */
    public void postProcess()
    {
    }

    public boolean isCancelled()
    {
        return cancelled;
    }

    public void setCancelled(boolean cancelled)
    {
        this.cancelled = cancelled;
    }

    @Deprecated
    public void resync()
    {
    }

    public Client getClient()
    {
        return client;
    }
}
