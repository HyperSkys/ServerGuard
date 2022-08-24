package dev.hyperskys.serverguard.events;

import dev.hyperskys.serverguard.ServerGuard;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class PlayerDamageEvent implements Listener {
    @EventHandler
    public void damageEvent(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            if (ServerGuard.getNoDamagePlayers().contains(player)) {
                event.setCancelled(true);
            }
        }
    }
}
