package dev.hyperskys.serverguard.events;

import dev.hyperskys.serverguard.ServerGuard;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerMoveEvent implements Listener {
    @EventHandler
    public void onMoveEvent(org.bukkit.event.player.PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (ServerGuard.getVerifyingPlayers().contains(player)) {
            event.setCancelled(true);
        }
    }
}
