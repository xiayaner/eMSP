package com.emsp.application;

import com.emsp.application.dto.AccountDTO;
import com.emsp.application.dto.AccountWithCardsDTO;
import com.emsp.application.dto.CardDTO;
import com.emsp.application.dto.CreateAccountRequest;
import com.emsp.application.dto.UpdateAccountStatusRequest;
import com.emsp.domain.model.Account;
import com.emsp.domain.model.Card;
import com.emsp.domain.service.AccountService;
import com.emsp.domain.service.CardService;
import com.emsp.infrastructure.event.DomainEventPublisher;
import com.emsp.infrastructure.persistence.converter.CardConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.emsp.infrastructure.persistence.converter.AccountConverter;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class AccountAppService {

    private final AccountService accountService;
    private final AccountConverter accountConverter;
    private final CardConverter cardConverter;
    private final DomainEventPublisher eventPublisher;
    private final CardService cardService;

    /**
     * 创建新账户
     *
     * @param request 创建账户请求
     * @return 创建的账户DTO
     */
    public AccountDTO createAccount(CreateAccountRequest request) {
        // 调用领域服务创建账户
        Account account = accountService.createAccount(request.getEmail());

        // 发布领域事件
        publishDomainEvents(account);

        // 转换为DTO并返回
        return accountConverter.toDTO(account);
    }

    /**
     * 更新账户状态
     *
     * @param accountId 账户ID
     * @param request 更新状态请求
     */
    public void updateAccountStatus(Long accountId, UpdateAccountStatusRequest request) {
        // 调用领域服务更新账户状态
        accountService.changeAccountStatus(accountId, request.getStatus());
    }

    /**
     * 根据最后更新时间查询账户
     *
     * @param lastUpdated 最后更新时间阈值
     * @param page 页码 (0-based)
     * @param size 每页大小
     * @return 分页的账户DTO
     */
    public Page<AccountWithCardsDTO> findAccountsWithCardsUpdatedAfter(
            Instant lastUpdated, int page, int size) {

        // 1. 分页查询账户
        List<Account> accounts = accountService.findByLastUpdatedAfter(lastUpdated, page, size);

        // 如果没有账户，直接返回空分页
        if (accounts.isEmpty()) {
            return new PageImpl<>(Collections.emptyList(), PageRequest.of(page, size), 0);
        }

        // 2. 获取账户ID列表
        List<Long> accountIds = accounts.stream()
                .map(Account::getId)
                .collect(Collectors.toList());

        // 3. 批量查询关联的卡片（按账户分组）
        Map<Long, List<Card>> cardsByAccountId = cardService.findCardsByAccountIds(accountIds);

        // 4. 转换为DTO
        List<AccountWithCardsDTO> dtos = accounts.stream()
                .map(account -> convertToAccountWithCardsDTO(account, cardsByAccountId.get(account.getId())))
                .collect(Collectors.toList());

        // 5. 创建分页对象（暂时不查询总记录数，避免性能问题）
        return new PageImpl<>(dtos, PageRequest.of(page, size), dtos.size());
    }

    private AccountWithCardsDTO convertToAccountWithCardsDTO(Account account, List<Card> cards) {
        AccountWithCardsDTO dto = new AccountWithCardsDTO();
        dto.setAccountId(account.getId());
        dto.setEmail(account.getEmail());
        dto.setStatus(account.getStatus());
        dto.setAccountCreatedDate(account.getCreatedDate());
        dto.setAccountLastUpdated(account.getLastUpdated());

        // 转换卡片列表
        if (cards != null && !cards.isEmpty()) {
            List<CardDTO> cardDTOs = cards.stream()
                    .map(cardConverter::toDTO)
                    .collect(Collectors.toList());
            dto.setCards(cardDTOs);
        } else {
            dto.setCards(Collections.emptyList());
        }

        return dto;
    }


    /**
     * 发布领域事件并清除
     *
     * @param account 账户对象
     */
    private void publishDomainEvents(Account account) {
        // 发布所有领域事件
        account.getDomainEvents().forEach(eventPublisher::publish);

        // 清除已发布的事件
        account.clearDomainEvents();
    }
}