package dev.hyperskys.serverguard.utils.color;

import net.md_5.bungee.api.ChatColor;

public class CC {
    public static String translate(String msg) {
        return ChatColor.translateAlternateColorCodes('&', msg);
    }
}
