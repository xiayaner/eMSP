package com.emsp.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AssignCardRequest {
    @Schema(description = "ID of the account to assign the card to",
            example = "123",
            required = true)
    @NotNull(message = "accountId is required")
    private Long accountId;
}
