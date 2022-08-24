package dev.hyperskys.serverguard.captcha;

import org.bukkit.entity.Player;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Random;
import java.util.stream.Collectors;

public class CaptchaUtils {

    private static final HashMap<Player, String> captcha = new HashMap<>();

    public static String generateCaptcha(Player player) {
        String password = new Random().ints(10, 33, 122).mapToObj(i -> String.valueOf((char)i)).collect(Collectors.joining());
        captcha.put(player, password);
        return password;
    }

    public static boolean verifyCaptcha(String input, Player player) {
        String origin = captcha.get(player);
        captcha.remove(player);
        return origin.equals(input);
    }
}
