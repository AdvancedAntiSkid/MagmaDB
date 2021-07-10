package net.bluenight.magmadb.client;

import net.bluenight.magmadb.client.query.Filter;

import java.util.ArrayList;
import java.util.List;

public class Query {
    private List<Filter> filters = new ArrayList<>();

    public Query add(Filter filter) {
        filters.add(filter);
        return this;
    }
}
