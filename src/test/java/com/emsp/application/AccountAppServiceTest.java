package com.emsp.application;

import com.emsp.infrastructure.persistence.converter.AccountConverter;
import com.emsp.application.dto.AccountDTO;
import com.emsp.application.dto.AccountWithCardsDTO;
import com.emsp.application.dto.CreateAccountRequest;
import com.emsp.application.dto.UpdateAccountStatusRequest;
import com.emsp.domain.model.Account;
import com.emsp.domain.model.AccountStatus;
import com.emsp.domain.model.Card;
import com.emsp.domain.model.CardStatus;
import com.emsp.domain.model.valueobjects.ContractId;
import com.emsp.domain.model.valueobjects.RFID;
import com.emsp.domain.service.AccountService;
import com.emsp.domain.service.CardService;
import com.emsp.infrastructure.event.DomainEventPublisher;
import com.emsp.infrastructure.persistence.converter.CardConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountAppServiceTest {

    @Mock
    private AccountService accountService;
    
    @Mock
    private AccountConverter accountConverter;

    @Mock
    private CardService cardService;

    @Mock
    private CardConverter cardConverter;

    @Mock
    private DomainEventPublisher eventPublisher;
    
    @InjectMocks
    private AccountAppService accountAppService;
    
    private Account testAccount;
    private AccountDTO testAccountDTO;
    private Map<Long, List<Card>> testCardsMap;
    
    @BeforeEach
    void setUp() {
        testAccount = Account.create("test@example.com");
        testAccount.setId(1L);
        
        testAccountDTO = new AccountDTO();
        testAccountDTO.setId(1L);
        testAccountDTO.setEmail("test@example.com");
        testAccountDTO.setStatus(AccountStatus.CREATED);
        List<Card> testCards = Arrays.asList(
                new Card(123L, new RFID("uid123", "21345"), new ContractId("OP234PQR234567"), CardStatus.CREATED, 1L, Instant.now(), Instant.now()),
                new Card(124L, new RFID("uid124", "21346"), new ContractId("OP234PQR234567"), CardStatus.ACTIVATED, 1L, Instant.now(), Instant.now())
        );
        testCardsMap = Collections.singletonMap(1L, testCards);
    }

    @Test
    void createAccount_shouldSuccess() {
        // 准备
        CreateAccountRequest request = new CreateAccountRequest();
        request.setEmail("test@example.com");
        
        when(accountService.createAccount(anyString())).thenReturn(testAccount);
        when(accountConverter.toDTO(any(Account.class))).thenReturn(testAccountDTO);
        
        // 执行
        AccountDTO result = accountAppService.createAccount(request);
        
        // 验证
        assertNotNull(result);
        assertEquals("test@example.com", result.getEmail());
        verify(accountService).createAccount("test@example.com");
    }

    @Test
    void updateAccountStatus_shouldSuccess() {
        // 准备
        UpdateAccountStatusRequest request = new UpdateAccountStatusRequest();
        request.setStatus(AccountStatus.ACTIVATED);
        
        // 执行
        accountAppService.updateAccountStatus(1L, request);
        
        // 验证
        verify(accountService).changeAccountStatus(1L, AccountStatus.ACTIVATED);
    }

    @Test
    void findAccountsUpdatedAfter_shouldReturnPage() {
        // 准备
        Instant testTime = Instant.now();
        PageRequest pageRequest = PageRequest.of(0, 10);

        when(accountService.findByLastUpdatedAfter(any(Instant.class), anyInt(), anyInt()))
            .thenReturn(Collections.singletonList(testAccount));
        when(cardService.findCardsByAccountIds(any())).thenReturn(testCardsMap);
        
        // 执行
        Page<AccountWithCardsDTO> result = accountAppService.findAccountsWithCardsUpdatedAfter(testTime, 0, 10);
        
        // 验证
        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals("test@example.com", result.getContent().get(0).getEmail());
    }
}