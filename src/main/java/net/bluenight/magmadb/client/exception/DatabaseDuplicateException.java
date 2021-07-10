package net.bluenight.magmadb.client.exception;

public class DatabaseDuplicateException extends RuntimeException {
    public DatabaseDuplicateException(String cause) {
        super(cause);
    }
}
