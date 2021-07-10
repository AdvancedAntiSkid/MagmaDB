package net.bluenight.magmadb.old;

import java.io.Serializable;

public class MyPacket implements Serializable
{
    private static final long serialVersionUID = 123234424324324234L;

    public int id;

    public MyPacket(int id)
    {
        this.id = id;
    }

    @Override
    public String toString()
    {
        return "MyPacket{" +
                "id=" + id +
                '}';
    }
}
