package net.bluenight.magmadb.old.server;

import net.bluenight.magmadb.old.util.Logger;

import java.io.*;
import java.nio.file.Paths;
import java.util.Base64;

public class FileStorage
{
    private File dataFolder;

    public FileStorage()
    {
        dataFolder = new File(Paths.get("").toFile(), "MagmaDB");
    }

    public void setup()
    {
        if (!dataFolder.isDirectory())
        {
            setup(dataFolder);
            Logger.info("Created data folder");
        }
    }

    private void setup(File folder)
    {
        if (!folder.mkdirs())
        {
            throw new RuntimeException("Unable to initialize storage");
        }
    }

    private <T> T load(File file, Class<T> clazz)
    {
        try
        {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            StringBuilder builder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null)
            {
                builder.append(line);
            }
            byte[] bytes = Base64.getDecoder().decode(builder.toString());
            ByteArrayInputStream stream = new ByteArrayInputStream(bytes);
            ObjectInputStream objectStream = new ObjectInputStream(stream);
            T object = (T) objectStream.readObject();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public File getDataFolder()
    {
        return dataFolder;
    }

    public void setDataFolder(File file)
    {
        this.dataFolder = file;
    }
}
