package net.bluenight.magmadb.old.server.packet;

import net.bluenight.magmadb.old.server.Client;

public interface PacketAdapter
{
    void run(Object packet, Client client);
}
