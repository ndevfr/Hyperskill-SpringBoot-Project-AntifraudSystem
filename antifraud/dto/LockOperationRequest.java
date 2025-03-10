package antifraud.dto;

import antifraud.enums.LockOperation;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class LockOperationRequest {
    @NotBlank
    private String username;
    @NotNull
    private LockOperation operation;

    public LockOperationRequest() {}

    public String getUsername() {
        return username;
    }

    public LockOperation getOperation() {
        return operation;
    }
}
