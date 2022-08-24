package dev.hyperskys.serverguard.events;

import dev.hyperskys.serverguard.ServerGuard;
import dev.hyperskys.serverguard.captcha.CaptchaUtils;
import dev.hyperskys.serverguard.mongodb.MongoDB;
import dev.hyperskys.serverguard.utils.LanguageUtils;
import dev.hyperskys.serverguard.utils.color.CC;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerChatEvent implements Listener {
    @EventHandler
    public void onChatEvent(org.bukkit.event.player.PlayerChatEvent event) {
        Player player = event.getPlayer();

        if (ServerGuard.getCaptchaPlayers().contains(player)) {
            if (CaptchaUtils.verifyCaptcha(event.getMessage(), player)) {
                player.playSound(player.getLocation(), Sound.VILLAGER_YES, 1, 1);
                for (Player player1 : Bukkit.getOnlinePlayers()) {
                    player.showPlayer(player1);
                    player1.showPlayer(player);
                }

                ServerGuard.getVerifyingPlayers().remove(player);
                MongoDB.addWhitelist(player);

                player.sendMessage(CC.translate("&7[&bServerGuard&7] &aYou are now verified, enjoy the server."));
                player.teleport(new Location(player.getWorld(), player.getLocation().getX(), player.getLocation().getY() + 2, player.getLocation().getZ()));
                player.removePotionEffect(PotionEffectType.BLINDNESS);
                ServerGuard.getAttempts().remove(player);

                ServerGuard.getNoDamagePlayers().add(player);
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        ServerGuard.getNoDamagePlayers().remove(player);
                    }
                }.runTaskLater(ServerGuard.getServerGuard(), 25);
            } else {
                player.kickPlayer(CC.translate("&cYou failed the captcha, you are most likely a robot."));
            }

            event.setCancelled(true);
            return;
        }

        if (MongoDB.checkWhitelist(player)) {
            if (MongoDB.verifyPassword(event.getMessage())) {
                if (LanguageUtils.captcha) {
                    player.playSound(player.getLocation(), Sound.NOTE_PIANO, 1, 1);
                    ServerGuard.getCaptchaPlayers().add(player);
                    player.sendMessage(CC.translate("&7[&bServerGuard&7] &aComplete the captcha to verify yourself."));
                    player.sendMessage(CC.translate("&7[&dCaptcha&7] &cPlease input this text in chat: &r") + "§e\"" + CaptchaUtils.generateCaptcha(player) + "\"");
                    event.setCancelled(true);
                } else {
                    player.playSound(player.getLocation(), Sound.VILLAGER_YES, 1, 1);
                    for (Player player1 : Bukkit.getOnlinePlayers()) {
                        player.showPlayer(player1);
                        player1.showPlayer(player);
                    }

                    MongoDB.addWhitelist(player);
                    player.sendMessage(CC.translate("&7[&bServerGuard&7] &aYou are now verified, enjoy the server."));
                    player.teleport(new Location(player.getWorld(), player.getLocation().getX(), player.getLocation().getY() + 2, player.getLocation().getZ()));
                    player.removePotionEffect(PotionEffectType.BLINDNESS);
                    ServerGuard.getAttempts().remove(player);

                    ServerGuard.getNoDamagePlayers().add(player);
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            ServerGuard.getNoDamagePlayers().remove(player);
                        }
                    }.runTaskLater(ServerGuard.getServerGuard(), 25);
                    event.setCancelled(true);
                }
            } else {
                if (ServerGuard.getAttempts().containsKey(player)) {
                    if (ServerGuard.getAttempts().get(player) != 1) {
                        player.playSound(player.getLocation(), Sound.VILLAGER_NO, 1, 1);
                        ServerGuard.getAttempts().put(player, ServerGuard.getAttempts().get(player) - 1);
                        player.sendMessage(CC.translate("&7[&bServerGuard&7] &cYou have entered an invalid password, " + (ServerGuard.getAttempts().get(player))  + " tries left."));
                    } else {
                        player.kickPlayer(CC.translate("&cYou got the server password wrong 3 times, try again."));
                        ServerGuard.getAttempts().remove(player);
                    }
                } else {
                    ServerGuard.getAttempts().put(player, 2);
                    player.sendMessage(CC.translate("&7[&bServerGuard&7] &cYou entered an invalid password, " + (ServerGuard.getAttempts().get(player))  + " tries left."));
                }

                event.setCancelled(true);
            }
        }
    }
}
