package net.bluenight.magmadb.old.packet.serverbound;

import net.bluenight.magmadb.old.packet.Packet;

import java.io.Serializable;

public class PacketInAuthenticate extends Packet implements Serializable
{
    private static final long serialVersionUID = -9182783657128391289L;

    private final String username;
    private final String password;
    private final String database;
    private final String data;

    public PacketInAuthenticate(String username, String password, String database, String data)
    {
        this.username = username;
        this.password = password;
        this.database = database;
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

    public String getDatabase()
    {
        return database;
    }

    public String getData()
    {
        return data;
    }

    @Override
    public String toString()
    {
        return "PacketInAuthenticate{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", database='" + database + '\'' +
                ", data='" + data + '\'' +
                '}';
    }
}
