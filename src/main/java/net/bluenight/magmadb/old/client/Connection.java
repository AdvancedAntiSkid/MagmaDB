package net.bluenight.magmadb.old.client;

import java.io.IOException;

public interface Connection
{
    void listen(boolean async) throws IOException;
    void listen() throws IOException;
    void close() throws IOException;
    boolean isOpen();
}
