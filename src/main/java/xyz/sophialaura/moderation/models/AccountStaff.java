package xyz.sophialaura.moderation.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Builder
@Data
public class AccountStaff {

    private boolean staffChat;
    private boolean adminChat;
    private boolean highStaffChat;

}
