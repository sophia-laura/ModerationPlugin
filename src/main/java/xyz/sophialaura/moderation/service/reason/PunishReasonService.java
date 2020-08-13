package xyz.sophialaura.moderation.service.reason;

import com.google.common.collect.ImmutableList;
import xyz.sophialaura.moderation.models.PunishReason;
import xyz.sophialaura.moderation.models.PunishType;

public interface PunishReasonService {

    ImmutableList<PunishReason> findAllByType(PunishType type);

    void add(PunishReason reason);

    void remove(PunishReason reason);

}
