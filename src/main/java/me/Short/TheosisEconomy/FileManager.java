package me.Short.TheosisEconomy;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class FileManager
{

    private File file;
    private FileConfiguration configFile;

    // Constructor
    public FileManager(File file)
    {
        this.file = file;
    }

    // Creates file if it doesn't exist and then loads it as a configuration file
    public void setup()
    {
        if (!file.exists())
        {
            try
            {
                file.getParentFile().mkdir();
                file.createNewFile();
            }
            catch (IOException exception)
            {
                exception.printStackTrace();
            }
        }
        configFile = YamlConfiguration.loadConfiguration(file);
    }

    // Gets the config file
    public FileConfiguration get()
    {
        return configFile;
    }

    // Re-load the config file from disk
    public void reload()
    {
        configFile = YamlConfiguration.loadConfiguration(file);
    }

    // Overwrites the file with the config file
    public void save()
    {
        try
        {
            this.configFile.save(file);
        }
        catch (IOException exception)
        {
            exception.printStackTrace();
        }
    }

}