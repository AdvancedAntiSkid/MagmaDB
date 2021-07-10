package net.bluenight.magmadb.old.packet;

import java.io.Serializable;

public class Packet implements Serializable
{
    private static final long serialVersionUID = -727293462364293847L;

    private int id;

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }
}
