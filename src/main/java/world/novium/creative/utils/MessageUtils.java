package world.novium.creative.utils;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;

public class MessageUtils {
    private static final MiniMessage mm = MiniMessage.miniMessage();
    private final static String prefix = "<gradient:#ff0000:#ff9900>Creative Hub</gradient><color:#30303d> • <color:#b2c2d4>";

    public static Component parseWithPrefix(String message) {
        return parse(prefix + " " + message);
    }

    public static Component parse(String message) {
        return mm.deserialize(message).decoration(TextDecoration.ITALIC, false);
    }

    public static void send(Audience audience, String message) {
        audience.sendMessage(parseWithPrefix(message));
    }

    public static void sendRaw(Audience audience, String message) {
        audience.sendMessage(parse(message));
    }
}
