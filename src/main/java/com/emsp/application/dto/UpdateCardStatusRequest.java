package com.emsp.application.dto;

import com.emsp.domain.model.CardStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class UpdateCardStatusRequest {
    @Schema(description = "New status for the card",
            example = "ACTIVATED",
            allowableValues = {"ASSIGNED", "ACTIVATED", "DEACTIVATED"},
            required = true)
    @NotNull(message = "Status is required")
    private CardStatus status;
}