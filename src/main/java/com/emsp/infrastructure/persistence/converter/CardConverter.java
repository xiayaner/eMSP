package com.emsp.infrastructure.persistence.converter;

import com.emsp.application.dto.CardDTO;
import com.emsp.domain.model.Account;
import com.emsp.domain.model.Card;
import com.emsp.domain.model.valueobjects.ContractId;
import com.emsp.domain.model.valueobjects.RFID;
import com.emsp.infrastructure.persistence.po.CardPO;
import org.springframework.stereotype.Component;

@Component
public class CardConverter {

    public CardPO toPO(Card card) {
        CardPO po = new CardPO();
        po.setId(card.getId());
        po.setUid(card.getRfid().getUid());
        po.setVisibleNumber(card.getRfid().getVisibleNumber());
        po.setContractId(card.getContractId().getValue());
        po.setStatus(card.getStatus());
        po.setAccountId(card.getAccountId());
        po.setCreatedDate(card.getCreatedDate());
        po.setLastUpdated(card.getLastUpdated());
        return po;
    }

    public Card toDomain(CardPO po) {
        RFID rfid = new RFID(po.getUid(), po.getVisibleNumber());
        ContractId contractId = new ContractId(po.getContractId());
        
        // 注意：这里Account是轻量级引用，仅包含ID
        // 实际使用时可能需要加载完整Account对象
        Account account = null;
        if (po.getAccountId() != null) {
            account = Account.reconstruct(po.getAccountId(), null, null, null, null);
        }
        
        return Card.reconstruct(
                po.getId(),
                rfid,
                contractId,
                po.getStatus(),
                po.getAccountId(),
                po.getCreatedDate(),
                po.getLastUpdated()
        );
    }

    public CardDTO toDTO(Card card) {
        CardDTO dto = new CardDTO();
        dto.setId(card.getId());
        dto.setUid(card.getRfid().getUid());
        dto.setVisibleNumber(card.getRfid().getVisibleNumber());
        dto.setContractId(card.getContractId().getValue());
        dto.setStatus(card.getStatus());
        dto.setAccountId(card.getAccountId());
        
        dto.setCreatedDate(card.getCreatedDate());
        dto.setLastUpdated(card.getLastUpdated());
        return dto;
    }
}