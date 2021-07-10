package net.bluenight.magmadb.client.element;

public class MagmaNull extends MagmaElement {
    public static final MagmaNull INSTANCE = new MagmaNull();

    @Deprecated
    public MagmaNull() {
    }

    @Override
    public MagmaElement deepCopy() {
        return INSTANCE;
    }

    @Override
    public String toString() {
        return "null";
    }

    @Override
    public boolean equals(Object object) {
        return object instanceof MagmaNull;
    }

    @Override
    public int hashCode() {
        return MagmaNull.class.hashCode();
    }
}
