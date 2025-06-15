package com.emsp.application.dto;

import com.emsp.domain.model.AccountStatus;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class UpdateAccountStatusRequest {
    @NotNull(message = "Status is required")
    private AccountStatus status;
}