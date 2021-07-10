package net.bluenight.magmadb.client.packet.serverbound;

import com.google.gson.JsonObject;
import net.bluenight.magmadb.client.ClientProfile;
import net.bluenight.magmadb.client.packet.Packet;

public class SPacketLogin implements Packet
{
    private String username;
    private String password;
    private String data;

    public SPacketLogin(String username, String password, String data)
    {
        this.username = username;
        this.password = password;
        this.data = data;
    }

    public SPacketLogin(ClientProfile profile)
    {
        this(profile.getUsername(), profile.getPassword(), profile.getData());
    }

    @Override
    public void read(JsonObject json)
    {
        username = json.get("username").getAsString();
        password = json.get("password").getAsString();
        data = json.get("data").getAsString();
    }

    @Override
    public void write(JsonObject json)
    {
        json.addProperty("username", username);
        json.addProperty("password", password);
        json.addProperty("data", data);
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

    @Override
    public String toString()
    {
        return "SPacketLogin{"
            + "username='" + username + '\''
            + ", password='" + password + '\''
            + ", data='" + data + '\''
            + '}';
    }
}
