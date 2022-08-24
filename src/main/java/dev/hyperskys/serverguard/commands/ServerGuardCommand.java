package dev.hyperskys.serverguard.commands;

import dev.hyperskys.serverguard.mongodb.MongoDB;
import dev.hyperskys.serverguard.utils.color.CC;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ServerGuardCommand implements CommandExecutor, TabExecutor {

    private final ArrayList<String> tabList = new ArrayList<>();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.isOp() || sender.hasPermission("sg.manage")) {
            if (args.length != 2) {
                sender.sendMessage(CC.translate("&8&m-+---------------------------------------+-"));
                sender.sendMessage(CC.translate("&d/sg <add> <player> &7# Adds a player to whitelist."));
                sender.sendMessage(CC.translate("&d/sg <remove> <player> &7# Removes a player from whitelist."));
                sender.sendMessage(CC.translate("&d/sg <setpassword> <password> &7# Sets the server password."));
                sender.sendMessage(CC.translate("&8&m-+---------------------------------------+-"));
                return true;
            }

            switch (args[0]) {
                case "add":
                    OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[1]);
                    if (MongoDB.addWhitelist(offlinePlayer.getUniqueId().toString(), offlinePlayer.getName())) {
                        sender.sendMessage(CC.translate("&7[&bServerGuard&7] &aYou added &e" + offlinePlayer.getName() + " &ato the whitelist."));
                        if (offlinePlayer.getPlayer() != null) offlinePlayer.getPlayer().kickPlayer(CC.translate("&aYou have been whitelisted by an admin, rejoin."));
                    } else {
                        sender.sendMessage(CC.translate("&7[&bServerGuard&7] &cThat player is already whitelisted."));
                    }
                    break;

                case "remove":
                    OfflinePlayer offlinePlayer1 = Bukkit.getOfflinePlayer(args[1]);
                    if (MongoDB.removeWhitelist(offlinePlayer1.getUniqueId().toString())) {
                        sender.sendMessage(CC.translate("&7[&bServerGuard&7] &cYou have removed &e" + offlinePlayer1.getName() + " &cfrom the whitelist."));
                        if (offlinePlayer1.getPlayer() != null) offlinePlayer1.getPlayer().kickPlayer(CC.translate("&cYou have been removed from the whitelist."));
                    } else {
                        sender.sendMessage(CC.translate("&7[&bServerGuard&7] &cThat player is not whitelisted."));
                    }
                    break;

                case "setpassword":
                    if (MongoDB.setPassword(args[1])) {
                        sender.sendMessage(CC.translate("&7[&bServerGuard&7] &aYou have changed the whitelist password."));
                        for (Player player : Bukkit.getOnlinePlayers()) {
                            if (!Objects.equals(player.getName(), sender.getName())) {
                                MongoDB.removeWhitelist(player.getUniqueId().toString());
                                player.kickPlayer(CC.translate("&cThe whitelist password was changed."));
                            }
                        }
                    } else {
                        sender.sendMessage(CC.translate("&7[&bServerGuard&7] &cThe password wasn't changed."));
                    }
                    break;

                default:
                    sender.sendMessage(CC.translate("&7[&bServerGuard&7] &cInvalid first argument provided, read usage."));
            }

            return true;
        }

        sender.sendMessage(CC.translate("&7[&bServerGuard&7] &cYou do not have permission to do this."));
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {

        if (args.length == 1) {
            tabList.clear();
            tabList.add("add");
            tabList.add("remove");
            tabList.add("setpassword");
        }

        if (args.length == 2) {
            tabList.clear();
            for (Player player : Bukkit.getOnlinePlayers()) tabList.add(player.getName());
        }

        return tabList;
    }
}
