package me.Short.TheosisEconomy.Commands;

import me.Short.TheosisEconomy.TheosisEconomy;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BalanceCommand implements TabExecutor
{

    // Instance of "TheosisEconomy"
    private TheosisEconomy instance;

    // Constructor
    public BalanceCommand(TheosisEconomy instance)
    {
        this.instance = instance;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command,	String label, String[] args)
    {
        if (args.length > 0)
        {
            if (sender.hasPermission("theosiseconomy.command.balance.others"))
            {
                // Try to get target player
                OfflinePlayer target = Bukkit.getPlayer(args[0]);
                if (target == null) // Target is not online, so now we check the cache to see if they have joined before...
                {
                    UUID uuid = instance.getPlayerCache().get(args[0]);
                    if (uuid != null)
                    {
                        target = Bukkit.getOfflinePlayer(uuid);
                    }
                }

                if (target != null)
                {
                    Economy economy = instance.getEconomy();

                    if (economy.hasAccount(target))
                    {
                        double balance = economy.getBalance(target);

                        if (target != sender)
                        {
                            instance.sendMiniMessage(sender, instance.getConfig().getString("messages.balance.their-balance"),
                                    Placeholder.component("target", Component.text(target.getName())),
                                    Placeholder.component("balance", Component.text(economy.format(balance))));
                        }
                        else
                        {
                            instance.sendMiniMessage(sender, instance.getConfig().getString("messages.balance.your-balance"),
                                    Placeholder.component("balance", Component.text(economy.format(balance))));
                        }
                    }
                    else
                    {
                        instance.sendMiniMessage(sender, instance.getConfig().getString("messages.error.no-account-other"),
                                Placeholder.component("target", Component.text(target.getName())));
                    }
                }
                else
                {
                    instance.sendMiniMessage(sender, instance.getConfig().getString("messages.error.not-joined-before"),
                            Placeholder.component("username", Component.text(args[0])));
                }
            }
            else
            {
                instance.sendMiniMessage(sender, instance.getConfig().getString("messages.error.no-permission"));
            }
        }
        else
        {
            if (sender instanceof Player)
            {
                Player player = (Player) sender;
                Economy economy = instance.getEconomy();

                if (economy.hasAccount(player))
                {
                    instance.sendMiniMessage(player, instance.getConfig().getString("messages.balance.your-balance"),
                            Placeholder.component("balance", Component.text(economy.format(economy.getBalance(player)))));
                }
                else
                {
                    instance.sendMiniMessage(player, instance.getConfig().getString("messages.error.no-account"));
                }
            }
            else
            {
                instance.sendMiniMessage(sender, instance.getConfig().getString("messages.error.console-cannot-use"));
            }
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args)
    {
        List<String> suggestions = new ArrayList<>();
        if (args.length == 1 && sender.hasPermission("theosiseconomy.command.balance.others"))
        {
            for (Player player : Bukkit.getOnlinePlayers())
            {
                String playerName = player.getName();
                if (playerName != null && playerName.toUpperCase().startsWith(args[0].toUpperCase()))
                {
                    suggestions.add(playerName);
                }
            }
        }

        return !suggestions.isEmpty() ? suggestions : null;
    }

}