package com.emsp.infrastructure.persistence.repository;

import com.emsp.domain.model.Card;
import com.emsp.domain.model.CardStatus;
import com.emsp.domain.model.valueobjects.RFID;
import com.emsp.domain.repository.CardRepository;
import com.emsp.infrastructure.persistence.converter.CardConverter;
import com.emsp.infrastructure.persistence.mapper.CardMapper;
import com.emsp.infrastructure.persistence.po.CardPO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class CardRepositoryImpl implements CardRepository {

    private final CardMapper cardMapper;
    private final CardConverter cardConverter;

    @Override
    public Card save(Card card) {
        CardPO po = cardConverter.toPO(card);
        if (card.getId() == null) {
            cardMapper.insert(po);
            card.setId(po.getId());
        } else {
            cardMapper.update(po);
        }
        return card;
    }

    @Override
    public Optional<Card> findById(String id) {
        return Optional.ofNullable(cardMapper.selectById(id))
                .map(cardConverter::toDomain);
    }

    @Override
    public Optional<Card> findByRfid(RFID rfid) {
        return Optional.ofNullable(cardMapper.selectByUid(rfid.getUid()))
                .map(cardConverter::toDomain);
    }

    @Override
    public List<Card> findByAccountId(Long accountId) {
        return cardMapper.selectByAccountId(accountId).stream()
                .map(cardConverter::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Card> findByLastUpdatedAfter(Instant lastUpdated, int offset, int size) {
        return cardMapper.selectByLastUpdatedAfter(lastUpdated, offset, size).stream()
                .map(cardConverter::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Card> findByAccountIdIn(List<Long> accountIds) {
        if (accountIds == null || accountIds.isEmpty()) {
            return Collections.emptyList();
        }

        // 查询PO
        List<CardPO> pos = cardMapper.selectByAccountIds(accountIds);

        // 转换为领域对象
        return pos.stream()
                .map(cardConverter::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Card> findByAccountIdAndStatus(Long accountId, CardStatus status) {
        // 查询PO
        List<CardPO> pos = cardMapper.selectByAccountIdAndStatus(accountId, status);

        // 转换为领域对象
        return pos.stream()
                .map(cardConverter::toDomain)
                .collect(Collectors.toList());
    }
}