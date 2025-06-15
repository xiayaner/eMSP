package com.emsp.domain.repository;

import com.emsp.domain.model.Card;
import com.emsp.domain.model.CardStatus;
import com.emsp.domain.model.valueobjects.ContractId;
import com.emsp.domain.model.valueobjects.RFID;
import com.emsp.infrastructure.persistence.converter.CardConverter;
import com.emsp.infrastructure.persistence.mapper.CardMapper;
import com.emsp.infrastructure.persistence.po.CardPO;
import com.emsp.infrastructure.persistence.repository.CardRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CardRepositoryImplTest {

    @Mock
    private CardMapper cardMapper;
    
    @Mock
    private CardConverter cardConverter;
    
    @InjectMocks
    private CardRepositoryImpl cardRepository;
    
    private Card testCard;
    private CardPO testCardPO;
    
    @BeforeEach
    void setUp() {
        testCard = Card.create(new RFID("uid123", "1234-5678"), new ContractId("QR567STU567890"));
        testCard.setId(123L);
        
        testCardPO = new CardPO();
        testCardPO.setId(123L);
        testCardPO.setUid("uid123");
        testCardPO.setVisibleNumber("1234-5678");
        testCardPO.setContractId("QR567STU567890");
        testCardPO.setStatus(CardStatus.CREATED);
    }

    @Test
    void save_shouldInsertNewCard() {
        // 准备
        when(cardConverter.toPO(any(Card.class))).thenReturn(testCardPO);
        
        // 执行
        Card saved = cardRepository.save(testCard);
        
        // 验证
        assertNotNull(saved);
        assertEquals(123L, saved.getId());
    }

    @Test
    void findById_shouldReturnCard() {
        // 准备
        when(cardMapper.selectById(anyString())).thenReturn(testCardPO);
        when(cardConverter.toDomain(any(CardPO.class))).thenReturn(testCard);
        
        // 执行
        Optional<Card> result = cardRepository.findById("card-123");
        
        // 验证
        assertTrue(result.isPresent());
        assertEquals("uid123", result.get().getRfid().getUid());
    }

    @Test
    void findByRfid_shouldReturnCard() {
        // 准备
        RFID rfid = new RFID("uid123", "1234-5678");
        when(cardMapper.selectByUid(anyString())).thenReturn(testCardPO);
        when(cardConverter.toDomain(any(CardPO.class))).thenReturn(testCard);
        
        // 执行
        Optional<Card> result = cardRepository.findByRfid(rfid);
        
        // 验证
        assertTrue(result.isPresent());
        assertEquals(123L, result.get().getId());
    }

    @Test
    void findByAccountId_shouldReturnList() {
        // 准备
        when(cardMapper.selectByAccountId(anyLong())).thenReturn(Collections.singletonList(testCardPO));
        when(cardConverter.toDomain(any(CardPO.class))).thenReturn(testCard);
        
        // 执行
        List<Card> result = cardRepository.findByAccountId(1L);
        
        // 验证
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("1234-5678", result.get(0).getRfid().getVisibleNumber());
    }

    @Test
    void findByLastUpdatedAfter_shouldReturnList() {
        // 准备
        Instant testTime = Instant.now();
        when(cardMapper.selectByLastUpdatedAfter(any(Instant.class), anyInt(), anyInt()))
            .thenReturn(Collections.singletonList(testCardPO));
        when(cardConverter.toDomain(any(CardPO.class))).thenReturn(testCard);
        
        // 执行
        List<Card> result = cardRepository.findByLastUpdatedAfter(testTime, 0, 10);
        
        // 验证
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("QR567STU567890", result.get(0).getContractId().getValue());
    }
}