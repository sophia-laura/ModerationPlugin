package xyz.sophialaura.moderation.utils;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import xyz.sophialaura.moderation.menu.api.ItemBuilder;

public class ConfigItemStack {

    public static ConfigItemBuilder findByName(FileConfiguration configuration, String key) {
        ItemBuilder builder = ItemBuilder.create(Material.getMaterial(configuration.getString(key + ".material")))
                .name(ChatColor.translateAlternateColorCodes('&', configuration.getString(key + ".display-name")));
        if (configuration.getBoolean(key + ".glow")) {
            builder.glow();
        }
        return new ConfigItemBuilder(builder, configuration.getString(key + ".name"));
    }

}
