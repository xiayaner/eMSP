package com.emsp.application;

import com.emsp.application.dto.AssignCardRequest;
import com.emsp.application.dto.CardDTO;
import com.emsp.application.dto.CreateCardRequest;
import com.emsp.application.dto.UpdateCardStatusRequest;
import com.emsp.domain.model.Card;
import com.emsp.domain.model.CardStatus;
import com.emsp.domain.model.valueobjects.ContractId;
import com.emsp.domain.model.valueobjects.RFID;
import com.emsp.domain.service.CardService;
import com.emsp.infrastructure.event.DomainEventPublisher;
import com.emsp.infrastructure.persistence.converter.CardConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CardAppServiceTest {

    @Mock
    private CardService cardService;
    
    @Mock
    private CardConverter cardConverter;

    @Mock
    private DomainEventPublisher eventPublisher;
    
    @InjectMocks
    private CardAppService cardAppService;
    
    private Card testCard;
    private CardDTO testCardDTO;
    
    @BeforeEach
    void setUp() {
        testCard = Card.create(new RFID("uid123", "1234-5678"), new ContractId("QR567STU567890"));
        testCard.setId(123L);
        
        testCardDTO = new CardDTO();
        testCardDTO.setId(123L);
        testCardDTO.setUid("uid123");
        testCardDTO.setVisibleNumber("1234-5678");
        testCardDTO.setContractId("QR567STU567890");
        testCardDTO.setStatus(CardStatus.CREATED);
    }

    @Test
    void createCard_shouldSuccess() {
        // 准备
        CreateCardRequest request = new CreateCardRequest();
        request.setUid("uid123");
        request.setVisibleNumber("1234-5678");
        request.setContractId("QR567STU567890");
        
        when(cardService.createCard(any(), any())).thenReturn(testCard);
        when(cardConverter.toDTO(any(Card.class))).thenReturn(testCardDTO);
        
        // 执行
        CardDTO result = cardAppService.createCard(request);
        
        // 验证
        assertNotNull(result);
        assertEquals("uid123", result.getUid());
        verify(cardService).createCard(any(), any());
    }

    @Test
    void assignCardToAccount_shouldSuccess() {
        when(cardService.getCardById(any())).thenReturn(testCard);
        // 执行
        cardAppService.assignCardToAccount("123", new AssignCardRequest(1L));
        
        // 验证
        verify(cardService).assignCardToAccount("123", 1L);
    }

    @Test
    void updateCardStatus_shouldSuccess() {
        // 准备
        UpdateCardStatusRequest request = new UpdateCardStatusRequest();
        request.setStatus(CardStatus.ACTIVATED);
        when(cardService.getCardById(any())).thenReturn(testCard);
        
        // 执行
        cardAppService.updateCardStatus("123", request);
        
        // 验证
        verify(cardService).changeCardStatus("123", CardStatus.ACTIVATED);
    }
}