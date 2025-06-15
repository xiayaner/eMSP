package com.emsp.application;

import com.emsp.application.dto.CardDTO;
import com.emsp.application.dto.AssignCardRequest;
import com.emsp.application.dto.CreateCardRequest;
import com.emsp.application.dto.UpdateCardStatusRequest;
import com.emsp.domain.model.Card;
import com.emsp.domain.model.CardStatus;
import com.emsp.domain.model.valueobjects.ContractId;
import com.emsp.domain.model.valueobjects.RFID;
import com.emsp.domain.service.CardService;
import com.emsp.infrastructure.event.DomainEventPublisher;
import com.emsp.infrastructure.persistence.converter.CardConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CardAppService {

    private final CardService cardService;
    private final CardConverter cardConverter;
    private final DomainEventPublisher eventPublisher;

    /**
     * 创建卡
     * @param request 创建卡请求
     * @return 创建的卡DTO
     */
    public CardDTO createCard(CreateCardRequest request) {
        Card card = cardService.createCard(
                new RFID(request.getUid(), request.getVisibleNumber()),
                new ContractId(request.getContractId())
        );
        
        // 发布领域事件
        card.getDomainEvents().forEach(eventPublisher::publish);
        card.clearDomainEvents();
        
        return cardConverter.toDTO(card);
    }

    /**
     * 将卡分配到账户
     * @param cardId 卡ID
     * @param request 分配请求
     */
    public void assignCardToAccount(String cardId, AssignCardRequest request) {
        cardService.assignCardToAccount(cardId, request.getAccountId());
        
        // 获取更新后的卡以发布事件
        Card card = cardService.getCardById(cardId);
        card.getDomainEvents().forEach(eventPublisher::publish);
        card.clearDomainEvents();
    }

    /**
     * 更新卡状态
     * @param cardId 卡ID
     * @param request 状态更新请求
     */
    public void updateCardStatus(String cardId, UpdateCardStatusRequest request) {
        CardStatus newStatus = request.getStatus();
        cardService.changeCardStatus(cardId, newStatus);
        
        // 获取更新后的卡以发布事件
        Card card = cardService.getCardById(cardId);
        card.getDomainEvents().forEach(eventPublisher::publish);
        card.clearDomainEvents();
    }

    /**
     * 根据最后更新时间查询卡
     * @param lastUpdated 最后更新时间
     * @param page 页码
     * @param size 每页大小
     * @return 卡DTO分页
     */
    public Page<CardDTO> findCardsUpdatedAfter(Instant lastUpdated, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        List<Card> cards = cardService.findCardsByLastUpdatedAfter(lastUpdated, page, size);
        
        List<CardDTO> dtos = cards.stream()
                .map(cardConverter::toDTO)
                .collect(Collectors.toList());
        
        return new PageImpl<>(dtos, pageRequest, dtos.size());
    }
}