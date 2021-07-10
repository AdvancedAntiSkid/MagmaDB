package net.bluenight.magmadb.client.exception;

public class NoSuchDatabaseException extends RuntimeException {
    public NoSuchDatabaseException(String cause) {
        super(cause);
    }
}
