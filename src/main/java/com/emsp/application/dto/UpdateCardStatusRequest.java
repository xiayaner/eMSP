package com.emsp.application.dto;

import com.emsp.domain.model.CardStatus;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class UpdateCardStatusRequest {
    @NotNull(message = "Status is required")
    private CardStatus status;
}