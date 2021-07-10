package net.bluenight.magmadb.old.packet.clientbound;

import net.bluenight.magmadb.old.packet.Packet;

public class PacketOutAuthenticate extends Packet
{
    private final boolean success;

    public PacketOutAuthenticate(boolean success)
    {
        this.success = success;
    }

    public boolean isSuccess()
    {
        return success;
    }

    @Override
    public String toString()
    {
        return "PacketOutAuthenticate{" +
                "success=" + success +
                '}';
    }
}
