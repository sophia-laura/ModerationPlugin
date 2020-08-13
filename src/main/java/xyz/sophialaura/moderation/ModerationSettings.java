package xyz.sophialaura.moderation;

import org.bukkit.configuration.file.FileConfiguration;
import xyz.sophialaura.moderation.utils.ConfigItemBuilder;
import xyz.sophialaura.moderation.utils.ConfigItemStack;

import java.util.ArrayList;
import java.util.List;

public class ModerationSettings {

    private static final ModerationPlugin plugin;
    private static final FileConfiguration configuration;

    static {
        plugin = ModerationPlugin.getInstance();
        configuration = plugin.getConfig();
    }

    public static long getReportExpirationTime() {
        return configuration.getInt("report.expiration-time") * 1000;
    }

    public static List<String> getReportMessage() {
        return configuration.getStringList("report.message");
    }

    public static List<ConfigItemBuilder> getReportReasonList() {
        List<ConfigItemBuilder> itemBuilders = new ArrayList<>();
        configuration.getConfigurationSection("report.reasons").getKeys(true).forEach(s -> itemBuilders
                .add(ConfigItemStack.findByName(configuration, s)));
        return itemBuilders;
    }
}
