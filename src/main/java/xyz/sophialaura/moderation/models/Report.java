package xyz.sophialaura.moderation.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@AllArgsConstructor
@Builder
@Data
public class Report {

    private final UUID author;
    private final UUID target;
    private final String reason;
    private long date;

}
