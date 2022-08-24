package dev.hyperskys.serverguard;

import dev.hyperskys.configurator.Configurator;
import dev.hyperskys.configurator.api.Configuration;
import dev.hyperskys.serverguard.commands.ServerGuardCommand;
import dev.hyperskys.serverguard.events.PlayerChatEvent;
import dev.hyperskys.serverguard.events.PlayerDamageEvent;
import dev.hyperskys.serverguard.events.PlayerJoinEvent;
import dev.hyperskys.serverguard.events.PlayerMoveEvent;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;

public final class ServerGuard extends JavaPlugin {

    private static final @Getter Configuration configs = new Configuration("config.yml");
    private static final @Getter ArrayList<Player> verifyingPlayers = new ArrayList<>();
    private static final @Getter ArrayList<Player> noDamagePlayers = new ArrayList<>();
    private static final @Getter ArrayList<Player> captchaPlayers = new ArrayList<>();
    private static final @Getter HashMap<Player, Integer> attempts = new HashMap<>();
    private static @Getter ServerGuard serverGuard;

    @Override
    public void onLoad() {
        serverGuard = this;
    }

    @Override
    public void onEnable() {
        Configurator.setupConfigurator(this);
        configs.init();
        Bukkit.getPluginManager().registerEvents(new PlayerJoinEvent(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerChatEvent(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerMoveEvent(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerDamageEvent(), this);
        getCommand("serverguard").setExecutor(new ServerGuardCommand());
    }
}
