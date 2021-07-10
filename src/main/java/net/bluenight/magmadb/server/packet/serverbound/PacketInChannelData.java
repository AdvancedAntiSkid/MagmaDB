package net.bluenight.magmadb.server.packet.serverbound;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import net.bluenight.magmadb.client.packet.Packet;

import java.util.ArrayList;
import java.util.List;

public class PacketInChannelData implements Packet
{
    private String channel;
    private List<String> args;

    @Override
    public void read(JsonObject json)
    {
        channel = json.get("channel").getAsString();
        JsonArray array = json.get("args").getAsJsonArray();
        args = new ArrayList<>(array.size());
        for (int i = 0; i < array.size(); i++)
        {
            args.set(i, array.get(i).getAsString());
        }
    }

    @Override
    public void write(JsonObject json)
    {
        json.addProperty("channel", channel);
        JsonArray array = new JsonArray(args.size());
        for (int i = 0; i < args.size(); i++)
        {
            array.set(i, new JsonPrimitive(args.get(i)));
        }
    }

    public String getChannel()
    {
        return channel;
    }

    public List<String> getArgs()
    {
        return args;
    }
}
