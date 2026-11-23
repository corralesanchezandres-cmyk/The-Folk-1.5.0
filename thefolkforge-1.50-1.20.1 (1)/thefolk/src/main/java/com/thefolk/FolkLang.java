package com.thefolk;

import net.minecraft.server.level.ServerPlayer;

import java.util.HashMap;
import java.util.Map;

/** Textos del juego en español e inglés. Para agregar un texto nuevo, añade una línea en add(...). */
public class FolkLang {
    public static final String ES = "es";
    public static final String EN = "en";

    private static final Map<String, String[]> TEXTS = new HashMap<>();

    static {
        add("behind", "atrás tuyo", "behind you");
        add("run", "CORRE", "RUN");
        add("coming", "Im C0MING", "Im C0MING");
        add("lang.set", "Idioma: Español", "Language: English");
    }

    private static void add(String key, String es, String en) {
        TEXTS.put(key, new String[]{es, en});
    }

    public static String langOf(ServerPlayer player) {
        return FolkLangData.get(player.serverLevel()).getLang(player.getUUID());
    }

    public static boolean isEnglish(ServerPlayer player) {
        return EN.equals(langOf(player));
    }

    public static String get(ServerPlayer player, String key) {
        String[] texts = TEXTS.get(key);
        if (texts == null) return key;
        return isEnglish(player) ? texts[1] : texts[0];
    }
}
