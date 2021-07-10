package net.bluenight.magmadb.old.server.event;

import net.bluenight.magmadb.old.server.Client;

public class ClientLoginEvent extends Event
{
    private final String username;
    private final String password;
    private final String data;

    public ClientLoginEvent(Client client, String username, String password, String data)
    {
        super(client);
        this.username = username;
        this.password = password;
        this.data = data;
    }

    public String getUsername()
    {
        return username;
    }

    public String getPassword()
    {
        return password;
    }

    public String getData()
    {
        return data;
    }
}
