package xyz.sophialaura.moderation.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import xyz.sophialaura.moderation.menu.api.ItemBuilder;

@AllArgsConstructor
@Getter
public class ConfigItemBuilder {

    private ItemBuilder builder;
    private String name;

}
