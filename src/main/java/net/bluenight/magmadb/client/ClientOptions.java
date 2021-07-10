package net.bluenight.magmadb.client;

public class ClientOptions
{
    public static final ClientOptions DEFAULT = new ClientOptions();

    private boolean reconnect;
    private long reconnectInterval;

    public boolean getReconnect()
    {
        return reconnect;
    }

    public ClientOptions setReconnect(boolean reconnect)
    {
        this.reconnect = reconnect;
        return this;
    }

    public long getReconnectInterval()
    {
        return reconnectInterval;
    }

    public ClientOptions setReconnectInterval(long reconnectInterval)
    {
        this.reconnectInterval = reconnectInterval;
        return this;
    }
}
