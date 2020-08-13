package xyz.sophialaura.moderation.models;

public enum PunishType {

    BAN, MUTE;

    public String getName() {
        return name().substring(0, 1).toUpperCase() + name().substring(1).toLowerCase();
    }

    public static PunishType findById(int id) {
        for (PunishType values : values()) {
            if (values.ordinal() == id) {
                return values;
            }
        }
        return null;
    }


}
