package com.emsp.domain.service;

import com.emsp.domain.model.Card;
import com.emsp.domain.model.CardStatus;
import com.emsp.domain.model.valueobjects.RFID;
import com.emsp.domain.model.valueobjects.ContractId;
import com.emsp.domain.repository.AccountRepository;
import com.emsp.domain.repository.CardRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CardServiceImpl implements CardService {

    private final CardRepository cardRepository;
    private final AccountRepository accountRepository;

    public CardServiceImpl(CardRepository cardRepository, AccountRepository accountRepository) {
        this.cardRepository = cardRepository;
        this.accountRepository = accountRepository;
    }

    @Override
    public Card createCard(RFID rfid, ContractId contractId) {
        // 检查UID是否唯一
        if (cardRepository.findByRfid(rfid).isPresent()) {
            throw new IllegalArgumentException("Card with this UID already exists");
        }
        
        return cardRepository.save(Card.create(rfid, contractId));
    }

    @Override
    public void assignCardToAccount(String cardId, Long accountId) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new IllegalArgumentException("Card not found"));
        
        card.assignToAccount(accountId);
        cardRepository.save(card);
    }

    @Override
    public void changeCardStatus(String cardId, CardStatus newStatus) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new IllegalArgumentException("Card not found"));
        
        switch (newStatus) {
            case ASSIGNED:
                // 分配操作由assignCardToAccount处理
                throw new IllegalArgumentException("Use assignCardToAccount for assignment");
            case ACTIVATED:
                card.activate();
                break;
            case DEACTIVATED:
                card.deactivate();
                break;
            default:
                throw new IllegalArgumentException("Invalid status transition");
        }
        
        cardRepository.save(card);
    }

    @Override
    public List<Card> findCardsByLastUpdatedAfter(Instant lastUpdated, int page, int size) {
        // 验证参数
        if (lastUpdated == null) {
            throw new IllegalArgumentException("Last updated timestamp cannot be null");
        }
        if (page < 0) {
            throw new IllegalArgumentException("Page number must be >= 0");
        }
        if (size <= 0) {
            throw new IllegalArgumentException("Page size must be > 0");
        }

        // 调用仓储实现分页查询
        return cardRepository.findByLastUpdatedAfter(lastUpdated, page, size);
    }

    @Override
    public Card getCardById(String cardId) {
        return cardRepository.findById(cardId).orElse(null);
    }

    @Override
    public Map<Long, List<Card>> findCardsByAccountIds(List<Long> accountIds) {
        if (accountIds == null || accountIds.isEmpty()) {
            return Collections.emptyMap();
        }

        // 查询所有关联的卡片
        List<Card> cards = cardRepository.findByAccountIdIn(accountIds);

        // 按账户ID分组
        return cards.stream()
                .filter(card -> card.getAccountId() != null)
                .collect(Collectors.groupingBy(Card::getAccountId));
    }
}