package xyz.sophialaura.moderation.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class PunishReason {

    private final String reason;
    private final PunishType type;
    private final long punishExpires;

    public boolean isExpired() {
        return punishExpires == -1;
    }
}
