package net.bluenight.magmadb.server.packet.serverbound;

import com.google.gson.JsonObject;
import net.bluenight.magmadb.server.packet.Packet;

public class PacketInAuthenticate implements Packet {
    private String username;
    private String password;
    private String data;

    @Override
    public void read(JsonObject json) {
        username = json.get("username").getAsString();
        password = json.get("password").getAsString();
        data = json.get("data").getAsString();
    }

    @Override
    public void write(JsonObject json) {
        json.addProperty("username", username);
        json.addProperty("password", password);
        json.addProperty("data", data);
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getData() {
        return data;
    }

    @Override
    public String toString() {
        return "PacketInAuthenticate{"
            + "username='" + username + '\''
            + ", password='" + password + '\''
            + ", data='" + data + '\''
            + '}';
    }
}
