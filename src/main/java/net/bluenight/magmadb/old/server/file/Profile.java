package net.bluenight.magmadb.old.server.file;

import net.bluenight.magmadb.old.server.Permission;

import java.io.Serializable;
import java.util.List;

public class Profile implements Serializable
{
    private static final long serialVersionUID = -4897267341877489134L;

    private final String username;
    private final String password;
    private final List<Permission> permissions;

    public Profile(String username, String password, List<Permission> permissions)
    {
        this.username = username;
        this.password = password;
        this.permissions = permissions;
    }

    public String getUsername()
    {
        return username;
    }

    public String getPassword()
    {
        return password;
    }

    public List<Permission> getPermissions()
    {
        return permissions;
    }
}
