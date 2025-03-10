package antifraud.dto;

import antifraud.enums.AppRole;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class RoleChangeRequest {
    @NotBlank
    private String username;
    @NotNull
    @Enumerated(EnumType.STRING)
    private AppRole role;

    public RoleChangeRequest() {}

    public String getUsername() {
        return username;
    }

    public AppRole getRole() {
        return role;
    }

    public boolean isAdmininistrator() {
        return role == AppRole.ADMINISTRATOR;
    }

}
