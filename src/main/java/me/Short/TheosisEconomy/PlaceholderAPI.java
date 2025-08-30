package me.Short.TheosisEconomy;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class PlaceholderAPI extends PlaceholderExpansion
{

    // Instance of "TheosisEconomy"
    private TheosisEconomy instance;

    // Constructor
    public PlaceholderAPI(TheosisEconomy instance)
    {
        this.instance = instance;
    }

    @Override
    public @NotNull String getIdentifier()
    {
        return instance.getDescription().getName();
    }

    @Override
    public @NotNull String getAuthor()
    {
        return instance.getDescription().getAuthors().get(0);
    }

    @Override
    public @NotNull String getVersion()
    {
        return instance.getDescription().getVersion();
    }

    @Override
    public boolean persist()
    {
        return true;
    }

    public static String setPAPIPlaceholders(OfflinePlayer player, String msg){
        return me.clip.placeholderapi.PlaceholderAPI.setPlaceholders(player, msg);
    }
    public static String setPAPIPlaceholders(CommandSender cmdSender, String msg){
        var p = cmdSender instanceof Player ? (Player)cmdSender : null;
        return me.clip.placeholderapi.PlaceholderAPI.setPlaceholders(p , msg);
    }

    @Override
    public String onRequest(OfflinePlayer player, String params)
    {
        // %theosiseconomy_accepting_payments%
        if (params.equalsIgnoreCase("accepting_payments"))
        {
            return Boolean.toString(instance.getPlayerAccounts().get(player.getUniqueId()).getAcceptingPayments());
        }

        // %theosiseconomy_combined_total_balance%
        if (params.equalsIgnoreCase("combined_total_balance"))
        {
            return instance.getCombinedTotalBalance().toPlainString();
        }

        // %theosiseconomy_combined_total_balance_formatted%
        if (params.equalsIgnoreCase("combined_total_balance_formatted"))
        {
            return instance.getCombinedTotalBalance().toPlainString();
        }

        // %theosiseconomy_richest_<position>_name%
        if (Pattern.compile("richest_[1-9][0-9]*_name$", Pattern.CASE_INSENSITIVE).matcher(params).find()) // If the placeholder follows the format "richest_<number>_name"...
        {
            try
            {
                int position = Integer.parseInt(StringUtils.replaceOnceIgnoreCase(StringUtils.replaceOnceIgnoreCase(params, "richest_", ""), "_name", ""));

                List<Map.Entry<String, BigDecimal>> baltopEntries = new ArrayList<>(instance.getBaltop().entrySet());

                if (position <= baltopEntries.size())
                {
                    return baltopEntries.get(position - 1).getKey();
                }

                return instance.getConfig().getString("settings.placeholders.baltop-position-name-none");
            }
            catch (NumberFormatException exception)
            {
                return null;
            }
        }

        // %theosiseconomy_richest_<position>_uuid%
        if (Pattern.compile("richest_[1-9][0-9]*_uuid$", Pattern.CASE_INSENSITIVE).matcher(params).find()) // If the placeholder follows the format "richest_<number>_uuid"...
        {
            try
            {
                int position = Integer.parseInt(StringUtils.replaceOnceIgnoreCase(StringUtils.replaceOnceIgnoreCase(params, "richest_", ""), "_uuid", ""));

                List<Map.Entry<String, BigDecimal>> baltopEntries = new ArrayList<>(instance.getBaltop().entrySet());

                if (position <= baltopEntries.size())
                {
                    return instance.getPlayerCache().get(baltopEntries.get(position - 1).getKey()).toString();
                }

                return instance.getConfig().getString("settings.placeholders.baltop-position-uuid-none");
            }
            catch (NumberFormatException exception)
            {
                return null;
            }
        }

        // %theosiseconomy_richest_<position>_balance%
        if (Pattern.compile("richest_[1-9][0-9]*_balance$", Pattern.CASE_INSENSITIVE).matcher(params).find()) // If the placeholder follows the format "richest_<number>_balance"...
        {
            try
            {
                int position = Integer.parseInt(StringUtils.replaceOnceIgnoreCase(StringUtils.replaceOnceIgnoreCase(params, "richest_", ""), "_balance", ""));

                List<Map.Entry<String, BigDecimal>> baltopEntries = new ArrayList<>(instance.getBaltop().entrySet());

                if (position <= baltopEntries.size())
                {
                    return baltopEntries.get(position - 1).getValue().toPlainString();
                }

                return instance.getConfig().getString("settings.placeholders.baltop-position-balance-none");
            }
            catch (NumberFormatException exception)
            {
                return null;
            }
        }

        // %theosiseconomy_richest_<position>_balance_formatted%
        if (Pattern.compile("richest_[1-9][0-9]*_balance_formatted$", Pattern.CASE_INSENSITIVE).matcher(params).find()) // If the placeholder follows the format "richest_<number>_balance_formatted"...
        {
            try
            {
                int position = Integer.parseInt(StringUtils.replaceOnceIgnoreCase(StringUtils.replaceOnceIgnoreCase(params, "richest_", ""), "_balance_formatted", ""));

                List<Map.Entry<String, BigDecimal>> baltopEntries = new ArrayList<>(instance.getBaltop().entrySet());

                if (position <= baltopEntries.size())
                {
                    return instance.getEconomy().format(baltopEntries.get(position - 1).getValue().doubleValue());
                }

                return instance.getConfig().getString("settings.placeholders.baltop-position-balance_formatted-none");
            }
            catch (NumberFormatException exception)
            {
                return null;
            }
        }

        // %theosiseconomy_richest_<position>_entry%
        if (Pattern.compile("richest_[1-9][0-9]*_entry$", Pattern.CASE_INSENSITIVE).matcher(params).find()) // If the placeholder follows the format "richest_<number>_entry"...
        {
            try
            {
                int position = Integer.parseInt(StringUtils.replaceOnceIgnoreCase(StringUtils.replaceOnceIgnoreCase(params, "richest_", ""), "_entry", ""));

                List<Map.Entry<String, BigDecimal>> baltopEntries = new ArrayList<>(instance.getBaltop().entrySet());

                if (position <= baltopEntries.size())
                {
                    String entryPlayerName = baltopEntries.get(position - 1).getKey();
                    Player entryPlayer = Bukkit.getPlayer(entryPlayerName);

                    String entry = instance.getConfig().getString(entryPlayer != null && player == entryPlayer ? "messages.baltop.entry-you" : "messages.baltop.entry")
                            .replace("<position>", Integer.toString(position))
                            .replace("<player>", entryPlayerName)
                            .replace("<balance>", instance.getEconomy().format(baltopEntries.get(position - 1).getValue().doubleValue()));

                    return entry
                            .replace("<dots>", new String(new char[instance.getNumberOfDotsToAlign(PlainTextComponentSerializer.plainText().serialize(instance.getMiniMessage().deserialize(entry)), true)]).replace("\0", "."));
                }

                return instance.getConfig().getString("settings.placeholders.baltop-position-entry-none");
            }
            catch (NumberFormatException exception)
            {
                return null;
            }
        }

        // %theosiseconomy_richest_<position>_entry_legacy%
        if (Pattern.compile("richest_[1-9][0-9]*_entry_legacy$", Pattern.CASE_INSENSITIVE).matcher(params).find()) // If the placeholder follows the format "richest_<number>_entry_legacy"...
        {
            try
            {
                int position = Integer.parseInt(StringUtils.replaceOnceIgnoreCase(StringUtils.replaceOnceIgnoreCase(params, "richest_", ""), "_entry_legacy", ""));

                List<Map.Entry<String, BigDecimal>> baltopEntries = new ArrayList<>(instance.getBaltop().entrySet());

                if (position <= baltopEntries.size())
                {
                    String entryPlayerName = baltopEntries.get(position - 1).getKey();
                    Player entryPlayer = Bukkit.getPlayer(entryPlayerName);

                    String entry = instance.getConfig().getString(entryPlayer != null && player == entryPlayer ? "messages.baltop.entry-you" : "messages.baltop.entry")
                            .replace("<position>", Integer.toString(position))
                            .replace("<player>", entryPlayerName)
                            .replace("<balance>", instance.getEconomy().format(baltopEntries.get(position - 1).getValue().doubleValue()));

                    return instance.getLegacyComponentSerializer().serialize(instance.getMiniMessage().deserialize(entry
                            .replace("<dots>", new String(new char[instance.getNumberOfDotsToAlign(PlainTextComponentSerializer.plainText().serialize(instance.getMiniMessage().deserialize(entry)), true)]).replace("\0", "."))));
                }

                return instance.getConfig().getString("settings.placeholders.baltop-position-entry-legacy-none");
            }
            catch (NumberFormatException exception)
            {
                return null;
            }
        }

        return null;
    }

}