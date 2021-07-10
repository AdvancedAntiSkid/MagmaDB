package net.bluenight.magmadb.old.client;

public class MagmaClient
{
    private final String host;
    private final int port;

    public MagmaClient(String host, int port)
    {
        this.host = host;
        this.port = port;
    }

    public Connection createConnection(ClientProfile profile, ClientOptions options)
    {
        return new ClientConnection(this, profile, options);
    }

    public Connection createConnection(ClientProfile profile)
    {
        return createConnection(profile, ClientOptions.DEFAULT);
    }

    public String getHost()
    {
        return host;
    }

    public int getPort()
    {
        return port;
    }
}
