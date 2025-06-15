package com.emsp.interfaces.rest;

import com.emsp.application.CardAppService;
import com.emsp.application.dto.CardDTO;
import com.emsp.application.dto.AssignCardRequest;
import com.emsp.application.dto.CreateCardRequest;
import com.emsp.application.dto.UpdateCardStatusRequest;
import lombok.RequiredArgsConstructor;
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
public class CardController {

    private final CardAppService cardAppService;

    @PostMapping
    public ResponseEntity<CardDTO> createCard(@Valid @RequestBody CreateCardRequest request) {
        CardDTO card = cardAppService.createCard(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(card);
    }

    @PatchMapping("/{cardId}/assign")
    public ResponseEntity<Void> assignCardToAccount(
            @PathVariable String cardId,
            @Valid @RequestBody AssignCardRequest request) {
        cardAppService.assignCardToAccount(cardId, request);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{cardId}/status")
    public ResponseEntity<Void> updateCardStatus(
            @PathVariable String cardId,
            @Valid @RequestBody UpdateCardStatusRequest request) {
        cardAppService.updateCardStatus(cardId, request);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<Page<CardDTO>> searchCards(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant lastUpdatedAfter,
            Pageable pageable) {
        Page<CardDTO> result = cardAppService.findCardsUpdatedAfter(
                lastUpdatedAfter, pageable.getPageNumber(), pageable.getPageSize());
        return ResponseEntity.ok(result);
    }
}