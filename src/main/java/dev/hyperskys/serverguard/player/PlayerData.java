package dev.hyperskys.serverguard.player;

import org.bukkit.entity.Player;

public class PlayerData {

    private int attempts;

    public int getTries() {
        return attempts;
    }

    public void addAttempts(int value) {
        attempts += value;
    }

    public void resetAttempts() {
        attempts = 0;
    }
}
