package com.emsp.interfaces.rest;

import com.emsp.application.CardAppService;
import com.emsp.application.dto.CardDTO;
import com.emsp.application.dto.AssignCardRequest;
import com.emsp.application.dto.CreateCardRequest;
import com.emsp.application.dto.UpdateCardStatusRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.Instant;

@RestController
@RequestMapping("/api/cards")
@RequiredArgsConstructor
@Tag(name = "Card Management", description = "APIs for managing cards")
public class CardController {

    private final CardAppService cardAppService;

    @PostMapping
    @Operation(summary = "Create a new card",
            description = "Creates a new card with the provided details")
    @ApiResponse(responseCode = "201", description = "Card created successfully",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CardDTO.class)))
    public ResponseEntity<CardDTO> createCard(@io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Card creation request",
            required = true,
            content = @Content(schema = @Schema(implementation = CreateCardRequest.class))) @Valid @RequestBody CreateCardRequest request) {
        CardDTO card = cardAppService.createCard(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(card);
    }

    @PatchMapping("/{cardId}/assign")
    @Operation(summary = "Assign a card to an account",
            description = "Assigns an existing card to an account")
    @ApiResponse(responseCode = "204", description = "Card assigned successfully")
    public ResponseEntity<Void> assignCardToAccount(
            @Parameter(description = "ID of the card to assign", required = true)
            @PathVariable String cardId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Card assignment request",
                    required = true,
                    content = @Content(schema = @Schema(implementation = AssignCardRequest.class)))
            @Valid @RequestBody AssignCardRequest request) {
        cardAppService.assignCardToAccount(cardId, request);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{cardId}/status")
    @Operation(summary = "Update card status",
            description = "Updates the status of an existing card")
    @ApiResponse(responseCode = "204", description = "Card status updated successfully")
    public ResponseEntity<Void> updateCardStatus(
            @Parameter(description = "ID of the card to update", required = true)
            @PathVariable String cardId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Card status update request",
                    required = true,
                    content = @Content(schema = @Schema(implementation = UpdateCardStatusRequest.class)))
            @Valid @RequestBody UpdateCardStatusRequest request) {
        cardAppService.updateCardStatus(cardId, request);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    @Operation(summary = "Search cards updated after a specific time",
            description = "Returns a paginated list of cards updated after the given timestamp")
    @ApiResponse(responseCode = "200", description = "Cards retrieved successfully",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CardDTO.class)))
    public ResponseEntity<Page<CardDTO>> searchCards(
            @Parameter(description = "Last updated timestamp (ISO format)", example = "2023-01-01T00:00:00Z", required = true)
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant lastUpdatedAfter,
            @ParameterObject Pageable pageable) {
        Page<CardDTO> result = cardAppService.findCardsUpdatedAfter(
                lastUpdatedAfter, pageable.getPageNumber(), pageable.getPageSize());
        return ResponseEntity.ok(result);
    }
}