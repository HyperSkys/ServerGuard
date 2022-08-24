package dev.hyperskys.serverguard.events;

import dev.hyperskys.serverguard.ServerGuard;
import dev.hyperskys.serverguard.mongodb.MongoDB;
import dev.hyperskys.serverguard.utils.color.CC;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PlayerJoinEvent implements Listener {
    @EventHandler
    public void onPlayerJoin(org.bukkit.event.player.PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (MongoDB.checkWhitelist(player)) {
            ServerGuard.getVerifyingPlayers().add(player);
            player.teleport(new Location(player.getWorld(), 696969, 420420, 696969));
            player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 200000, 255, false, false));

            for (Player player1 : Bukkit.getOnlinePlayers()) {
                player1.hidePlayer(player);
                player.hidePlayer(player1);
            }

            player.sendMessage(CC.translate("&8&m-+---------------------------------------+-"));
            player.sendMessage(CC.translate("&cYou have not been verified, and this server"));
            player.sendMessage(CC.translate("&crequires verification input password below."));
            player.sendMessage(CC.translate("&8&m-+---------------------------------------+-"));
            player.sendMessage(CC.translate(" "));
        }

        for (Player player1 : ServerGuard.getVerifyingPlayers()) {
            for (Player all : Bukkit.getOnlinePlayers()) {
                all.hidePlayer(player1);
                player1.hidePlayer(all);
            }
        }
    }
}
