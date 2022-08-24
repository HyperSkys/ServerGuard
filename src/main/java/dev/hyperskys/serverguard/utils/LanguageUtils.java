package dev.hyperskys.serverguard.utils;

import dev.hyperskys.configurator.annotations.GetValue;

public class LanguageUtils {
    public static @GetValue(file = "config.yml", path = "Settings.mongodb-uri") String mongoURI = "mongodb://user:password@localhost:27017";
    public static @GetValue(file = "config.yml", path = "Settings.captcha-enabled") boolean captcha = true;
    public static @GetValue(file = "config.yml", path = "Settings.captcha-type") String captchaType = "Coming Soon";
}
