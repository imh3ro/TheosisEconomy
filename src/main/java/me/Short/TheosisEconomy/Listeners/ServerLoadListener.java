package me.Short.TheosisEconomy.Listeners;

import me.Short.TheosisEconomy.TheosisEconomy;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerLoadEvent;

public class ServerLoadListener implements Listener
{

    // Instance of "TheosisEconomy"
    private TheosisEconomy instance;

    // Constructor
    public ServerLoadListener(TheosisEconomy instance)
    {
        this.instance = instance;
    }

    @EventHandler
    public void onServerLoad(ServerLoadEvent event)
    {
        // Populate "playerCache" - this is here in "onServerLoad" instead of "onEnable" because in some older Spigot versions, Bukkit#getOfflinePlayers produces a NPE error
        for (OfflinePlayer player : Bukkit.getOfflinePlayers())
        {
            instance.getPlayerCache().put(player.getName(), player.getUniqueId());
        }
    }

}