import net.bluenight.magmadb.old.MyPacket;
import net.bluenight.magmadb.old.client.ClientConnection;
import net.bluenight.magmadb.old.client.Connection;
import net.bluenight.magmadb.old.server.MagmaServer;
import net.bluenight.magmadb.old.client.MagmaClient;
import net.bluenight.magmadb.old.client.ClientProfile;

public class MainOld
{
    public static void main(String[] args) throws Exception
    {
        MagmaServer server = new MagmaServer(100);
        //server.setWorkingDirectory("C:\\Users\\Admin\\IdeaProjects\\MagmaDB\\MagmaDB");
        server.listen();

        MagmaClient client = new MagmaClient("127.0.0.1", 100);
        ClientProfile profile = new ClientProfile("admin", "12345");
        Connection connection = client.createConnection(profile);

        connection.listen();



        ((ClientConnection) connection).getPacketHandler().getPacketListener().getStreamOut().writeObject(new MyPacket(1337));

        ((ClientConnection) connection).getPacketHandler().getPacketListener().getStreamOut().flush();
    }
}
