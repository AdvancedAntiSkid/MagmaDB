package net.bluenight.magmadb.client.exception;

public class MagmaDBException extends RuntimeException {
    public MagmaDBException(String cause) {
        super(cause);
    }

    public MagmaDBException(String cause, Throwable throwable) {
        super(cause, throwable);
    }
}
