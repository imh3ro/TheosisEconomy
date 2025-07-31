package me.Short.TheosisEconomy.Commands;

import me.Short.TheosisEconomy.Events.PlayerPayPlayerEvent;
import me.Short.TheosisEconomy.TheosisEconomy;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import net.milkbowl.vault.economy.EconomyResponse.ResponseType;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PayCommand implements TabExecutor
{

    // Instance of "TheosisEconomy"
    private TheosisEconomy instance;

    // Constructor
    public PayCommand(TheosisEconomy instance)
    {
        this.instance = instance;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (sender instanceof Player)
        {
            if (args.length > 0)
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
                    if (sender != target)
                    {
                        Player player = (Player) sender;
                        Economy economy = instance.getEconomy();

                        if (economy.hasAccount((player)))
                        {
                            if (economy.hasAccount(target))
                            {
                                if (instance.getPlayerAccounts().get(target.getUniqueId()).getAcceptingPayments())
                                {
                                    if (args.length > 1)
                                    {
                                        // Try to parse BigDecimal amount from the command argument
                                        BigDecimal amount;
                                        try
                                        {
                                            amount = new BigDecimal(args[1]);
                                        }
                                        catch (NumberFormatException exception)
                                        {
                                            instance.sendMiniMessage(sender, instance.getConfig().getString("messages.error.invalid-amount"),
                                                    Placeholder.component("amount", Component.text(args[1])));

                                            return true;
                                        }

                                        // Call PlayerPayPlayerEvent event
                                        PlayerPayPlayerEvent playerPayPlayerEvent = new PlayerPayPlayerEvent(player, target, amount);
                                        Bukkit.getServer().getPluginManager().callEvent(playerPayPlayerEvent);

                                        if (!playerPayPlayerEvent.isCancelled())
                                        {
                                            // Reassign variables in case a plugin listening for the event changed them
                                            player = playerPayPlayerEvent.getSender();
                                            target = playerPayPlayerEvent.getRecipient();
                                            amount = playerPayPlayerEvent.getAmount();

                                            double amountAsDouble = amount.doubleValue(); // For passing into the Economy methods, as they only take doubles

                                            // Try to withdraw the amount from the payer
                                            EconomyResponse withdrawPlayerResponse = economy.withdrawPlayer(player, amountAsDouble);

                                            if (withdrawPlayerResponse.type == ResponseType.SUCCESS)
                                            {
                                                // Try to deposit the amount to the target
                                                EconomyResponse depositPlayerResponse = economy.depositPlayer(target, amountAsDouble);

                                                if (depositPlayerResponse.type == ResponseType.SUCCESS)
                                                {
                                                    FileConfiguration config = instance.getConfig();

                                                    String amountFormatted = economy.format(depositPlayerResponse.amount);

                                                    // Send message to the target player, if online
                                                    if (target.isOnline())
                                                    {
                                                        instance.sendMiniMessage(target.getPlayer(), config.getString("messages.pay.paid-target"),
                                                                Placeholder.component("player", Component.text(player.getName())),
                                                                Placeholder.component("amount", Component.text(amountFormatted)));
                                                    }

                                                    // Send message to the command sender
                                                    instance.sendMiniMessage(player, config.getString("messages.pay.paid-sender"),
                                                            Placeholder.component("target", Component.text(target.getName())),
                                                            Placeholder.component("amount", Component.text(amountFormatted)));
                                                }
                                                else // If the deposit was unsuccessful...
                                                {
                                                    // Deposit the amount back into the payer's account
                                                    economy.depositPlayer(player, amountAsDouble);

                                                    // Send error message - this is the only possible error, since the other errors were ruled out because the withdrawal was successful
                                                    instance.sendMiniMessage(player, instance.getConfig().getString("messages.error.would-exceed-max-balance"),
                                                            Placeholder.component("target", Component.text(target.getName())));
                                                }
                                            }
                                            else // If the withdrawal was unsuccessful...
                                            {
                                                String errorMessage = withdrawPlayerResponse.errorMessage;

                                                if (errorMessage.equals(me.Short.TheosisEconomy.Economy.getErrorInsufficientFunds()))
                                                {
                                                    instance.sendMiniMessage(player, instance.getConfig().getString("messages.error.insufficient-funds"),
                                                            Placeholder.component("amount", Component.text(economy.format(amountAsDouble))));
                                                }
                                                else if (errorMessage.equals(me.Short.TheosisEconomy.Economy.getErrorTooManyDecimalPlaces()))
                                                {
                                                    instance.sendMiniMessage(player, instance.getConfig().getString("messages.error.too-many-decimal-places-amount"),
                                                            Placeholder.component("amount", Component.text(amount.toPlainString())),
                                                            Placeholder.component("decimal_places", Component.text(economy.fractionalDigits())));
                                                }
                                                else if (errorMessage.equals(me.Short.TheosisEconomy.Economy.getErrorNotGreaterThanZero()))
                                                {
                                                    instance.sendMiniMessage(player, instance.getConfig().getString("messages.error.not-greater-than-zero-amount"));
                                                }
                                            }
                                        }
                                        else
                                        {
                                            return true;
                                        }
                                    }
                                    else
                                    {
                                        instance.sendMiniMessage(player, instance.getConfig().getString("messages.error.incorrect-usage"),
                                                Placeholder.component("usage", Component.text("/pay <player> <amount>")));
                                    }
                                }
                                else
                                {
                                    instance.sendMiniMessage(player, instance.getConfig().getString("messages.error.not-accepting-payments"),
                                            Placeholder.component("target", Component.text(target.getName())));
                                }
                            }
                            else
                            {
                                instance.sendMiniMessage(player, instance.getConfig().getString("messages.error.no-account-other"),
                                        Placeholder.component("target", Component.text(target.getName())));
                            }
                        }
                        else
                        {
                            instance.sendMiniMessage(player, instance.getConfig().getString("messages.error.no-account"));
                        }
                    }
                    else
                    {
                        instance.sendMiniMessage(sender, instance.getConfig().getString("messages.error.cannot-pay-yourself"));
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
                instance.sendMiniMessage(sender, instance.getConfig().getString("messages.error.incorrect-usage"),
                        Placeholder.component("usage", Component.text("/pay <player> <amount>")));
            }
        }
        else
        {
            instance.sendMiniMessage(sender, instance.getConfig().getString("messages.error.console-cannot-use"));
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args)
    {
        List<String> suggestions = new ArrayList<>();
        if (args.length == 1 && sender instanceof Player)
        {
            for (Player player : Bukkit.getOnlinePlayers())
            {
                String playerName = player.getName();
                if (playerName != null && playerName.toUpperCase().startsWith(args[0].toUpperCase()))
                {
                    suggestions.add(playerName);
                }
            }
            suggestions.remove(sender.getName());
        }

        return !suggestions.isEmpty() ? suggestions : null;
    }

}