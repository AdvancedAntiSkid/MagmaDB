import net.bluenight.magmadb.client.ClientProfile;
import net.bluenight.magmadb.client.MagmaClient;
import net.bluenight.magmadb.client.element.Collection;
import net.bluenight.magmadb.client.element.Database;
import net.bluenight.magmadb.client.element.Element;
import net.bluenight.magmadb.server.MagmaServer;

public class Main {
    public static void main(String[] args) throws Exception {
        MagmaServer server = new MagmaServer(100);
        server.listen();

        ClientProfile profile = new ClientProfile("admin", "123");
        MagmaClient client = new MagmaClient("127.0.0.1", 100, profile);
        client.listen();

        Database database;
        if (!client.hasDatabase("test")) {
            database = client.createDatabase("test");
            System.err.println("not found, creating one");
        } else {
            database = client.getDatabase("test");
            System.err.println("found");
        }

        Collection collection;
        if (!database.hasCollection("wtf")) {
            collection = database.createCollection("wtf");
        } else {
            collection = database.getCollection("wtf");
        }

        collection.updateOne("e.name == 'John Doe'", "e.id = 123");
        Element element = collection.findOne("e.name == 'John Doe'");


        System.err.println("my element " + element);
    }
}
