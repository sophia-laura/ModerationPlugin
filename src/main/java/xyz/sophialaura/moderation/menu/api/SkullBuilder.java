package xyz.sophialaura.moderation.menu.api;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;
import org.apache.commons.codec.binary.Base64;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

public class SkullBuilder {

    private final ItemStack item;

    private SkullBuilder(ItemStack item) {
        this.item = item;
    }

    public static SkullBuilder create() {
        return new SkullBuilder(new ItemStack(Material.SKULL_ITEM, 1, (short) 3));
    }

    public SkullBuilder changeItem(Consumer<ItemStack> consumer) {
        consumer.accept(this.item);
        return this;
    }

    public SkullBuilder changeMeta(Consumer<SkullMeta> consumer) {
        final SkullMeta itemMeta = (SkullMeta) this.item.getItemMeta();
        if (itemMeta != null) {
            consumer.accept(itemMeta);
            this.item.setItemMeta(itemMeta);
        }
        return this;
    }

    public SkullBuilder name(String displayName) {
        return changeMeta(itemMeta -> itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', displayName)));
    }

    public SkullBuilder lore(String... lore) {
        return changeMeta(itemMeta -> itemMeta.setLore(Arrays.asList(lore)));
    }

    public SkullBuilder lore(Consumer<List<String>> consumer) {
        List<String> lore = new ArrayList<>();
        consumer.accept(lore);
        return changeMeta(itemMeta -> itemMeta.setLore(lore));
    }

    public SkullBuilder amount(int amount) {
        return changeItem(itemStack -> itemStack.setAmount(amount));
    }

    public SkullBuilder owner(OfflinePlayer offlinePlayer) {
        this.changeMeta(skullMeta -> skullMeta.setOwner(offlinePlayer.getName()));
        return this;
    }

    public SkullBuilder owner(String name) {
        this.changeMeta(skullMeta -> skullMeta.setOwner(name));
        return this;
    }

    public SkullBuilder texture(String texture) {
        changeMeta(
                skullMeta -> {
                    GameProfile profile = new GameProfile(UUID.randomUUID(), null);
                    PropertyMap propertyMap = profile.getProperties();
                    if (propertyMap != null) {
                        byte[] encodedData =
                                Base64.encodeBase64(
                                        String.format("{textures:{SKIN:{url:\"%s\"}}}", texture).getBytes());
                        propertyMap.put("textures", new Property("textures", new String(encodedData)));

                        try {
                            Field field = skullMeta.getClass().getDeclaredField("profile");
                            field.setAccessible(true);
                            field.set(skullMeta, profile);
                        } catch (NoSuchFieldException | IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }
                });
        return this;
    }

    public ItemStack build() {
        return this.item;
    }
}
