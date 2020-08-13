package xyz.sophialaura.moderation.service.reason;

import com.google.common.collect.ImmutableList;
import xyz.sophialaura.moderation.models.PunishReason;
import xyz.sophialaura.moderation.models.PunishType;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class PunishReasonServiceImpl implements PunishReasonService {

    private final Set<PunishReason> reasons;

    public PunishReasonServiceImpl() {
        reasons = new HashSet<>();
    }

    @Override
    public ImmutableList<PunishReason> findAllByType(PunishType type) {
        return ImmutableList.copyOf(reasons.stream().filter(reason -> reason.getType() == type).collect(Collectors.toSet()));
    }

    @Override
    public void add(PunishReason reason) {
        this.reasons.add(reason);
    }

    @Override
    public void remove(PunishReason reason) {
        this.reasons.remove(reason);
    }
}
