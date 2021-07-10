package net.bluenight.magmadb.old.server;

import java.io.Serializable;

public class Permission implements Serializable
{
    private static final long serialVersionUID = -3749323658787128473L;

    private final String target;
    private final PermissionType type;

    public Permission(String target, PermissionType type)
    {
        this.target = target;
        this.type = type;
    }

    public String getTarget()
    {
        return target;
    }

    public PermissionType getType()
    {
        return type;
    }

    public enum PermissionType implements Serializable
    {
        READ_COLLECTION, WRITE_COLLECTION, MANAGE_COLLECTION;
        private static final long serialVersionUID = -981254747918549572L;
    }
}
