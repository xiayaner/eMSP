package com.emsp.application.dto;

import com.emsp.domain.model.AccountStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class UpdateAccountStatusRequest {
    @Schema(description = "New status for the account",
            example = "DEACTIVATED",
            allowableValues = {"ACTIVATED", "DEACTIVATED"},
            required = true)
    @NotNull(message = "Status is required")
    private AccountStatus status;
}