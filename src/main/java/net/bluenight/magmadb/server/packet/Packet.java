package net.bluenight.magmadb.server.packet;

import com.google.gson.JsonObject;

public interface Packet
{
    void read(JsonObject json);

    void write(JsonObject json);
}
