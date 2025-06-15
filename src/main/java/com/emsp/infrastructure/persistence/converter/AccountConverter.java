package com.emsp.infrastructure.persistence.converter;

import com.emsp.application.dto.AccountDTO;
import com.emsp.domain.model.Account;
import com.emsp.infrastructure.persistence.po.AccountPO;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class AccountConverter {

    private final CardConverter cardConverter;

    public AccountConverter(CardConverter cardConverter) {
        this.cardConverter = cardConverter;
    }

    /**
     * 将领域对象 Account 转换为 AccountDTO
     *
     * @param account 领域对象
     * @return 对应的 DTO 对象
     */
    public AccountDTO toDTO(Account account) {
        if (account == null) {
            return null;
        }

        AccountDTO dto = new AccountDTO();
        dto.setId(account.getId());
        dto.setEmail(account.getEmail());
        dto.setStatus(account.getStatus());
        dto.setCreatedDate(account.getCreatedDate());
        dto.setLastUpdated(account.getLastUpdated());

        // 转换关联的卡对象
        if (account.getCards() != null) {
            dto.setCards(account.getCards().stream()
                    .map(cardConverter::toDTO)
                    .collect(Collectors.toList()));
        }

        return dto;
    }


    public Account toDomain(AccountPO po) {
        if (po == null) {
            return null;
        }

        return Account.reconstruct(
                po.getId(),
                po.getEmail(),
                po.getStatus(),
                po.getCreatedDate(),
                po.getLastUpdated()
        );
    }

    public AccountPO toPO(Account account) {
        AccountPO po = new AccountPO();
        po.setId(account.getId());
        po.setEmail(account.getEmail());
        po.setStatus(account.getStatus());
        po.setCreatedDate(account.getCreatedDate());
        po.setLastUpdated(account.getLastUpdated());
        return po;
    }
}