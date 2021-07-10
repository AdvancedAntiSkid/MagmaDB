package net.bluenight.magmadb.old.client;

public class ClientProfile
{
    private String username;
    private String password;
    private String database;

    public ClientProfile(String username, String password, String database)
    {
        this.username = username;
        this.password = password;
        this.database = database;
    }

    public ClientProfile(String username, String password)
    {
        this(username, password, "*");
    }

    public String getUsername()
    {
        return username;
    }

    public ClientProfile setUsername(String username)
    {
        this.username = username;
        return this;
    }

    public String getPassword()
    {
        return password;
    }

    public ClientProfile setPassword(String password)
    {
        this.password = password;
        return this;
    }

    public String getDatabase()
    {
        return database;
    }

    public ClientProfile setDatabase(String database)
    {
        this.database = database;
        return this;
    }
}
