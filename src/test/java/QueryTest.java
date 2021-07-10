import net.bluenight.magmadb.client.Query;
import net.bluenight.magmadb.client.query.Filter;

public class QueryTest {
    public static void main(String[] args) {
        Query query = new Query()
            .add(Filter.eq("user.group", "Admin"))
            .add(Filter.notEq("user.status", "Banned"));
    }
}
