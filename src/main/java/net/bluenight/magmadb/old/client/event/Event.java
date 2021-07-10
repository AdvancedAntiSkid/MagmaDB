package net.bluenight.magmadb.old.client.event;

public class Event
{
    protected boolean cancelled;

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
}
