package me.Short.TheosisEconomy.Events;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.Set;
import java.util.UUID;

public class PreBaltopSortEvent extends Event implements Cancellable
{
    private static final HandlerList handlers = new HandlerList();

    private boolean cancelled;

    // Set of UUIDs of players who will be excluded from baltop
    private Set<UUID> excludedPlayers;

    public PreBaltopSortEvent(Set<UUID> excludedPlayers)
    {
        this.excludedPlayers = excludedPlayers;
    }

    @Override
    public boolean isCancelled()
    {
        return this.cancelled;
    }

    @Override
    public void setCancelled(boolean cancel)
    {
        this.cancelled = cancel;
    }

    @Override
    public HandlerList getHandlers()
    {
        return handlers;
    }

    public static HandlerList getHandlerList()
    {
        return handlers;
    }

    // ----- Getters -----

    // Getter for "excludedPlayers"
    public Set<UUID> getExcludedPlayers()
    {
        return excludedPlayers;
    }

    // ----- Setters -----

    // Setter for "excludedPlayers"
    public void setExcludedPlayers(Set<UUID> excludedPlayers)
    {
        this.excludedPlayers = excludedPlayers;
    }

}