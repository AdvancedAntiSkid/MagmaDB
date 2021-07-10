package net.bluenight.magmadb.client;

public class ClientProfile
{
    public static final ClientProfile DEFAULT = new ClientProfile("admin", "", "");

    private String username;
    private String password;
    private String data;

    public ClientProfile(String username, String password, String data)
    {
        this.username = username;
        this.password = password;
        this.data = data;
    }

    public ClientProfile(String username, String password)
    {
        this(username, password, "");
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public String getData()
    {
        return data;
    }

    public void setData(String data)
    {
        this.data = data;
    }
}
