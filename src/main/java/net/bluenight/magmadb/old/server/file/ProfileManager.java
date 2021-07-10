package net.bluenight.magmadb.old.server.file;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ProfileManager implements Serializable
{
    private static final long serialVersionUID = -2128934042427822348L;

    private final List<Profile> profiles;

    public ProfileManager()
    {
        profiles = new ArrayList<>();
    }

    public Profile getProfile(String username)
    {
        return profiles.stream().filter(profile
            -> profile.getUsername().equals(username)).findFirst().orElse(null);
    }

    public List<Profile> getProfiles()
    {
        return profiles;
    }
}
