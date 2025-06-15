package com.emsp.domain.repository;

import com.emsp.infrastructure.persistence.converter.AccountConverter;
import com.emsp.domain.model.Account;
import com.emsp.domain.model.AccountStatus;
import com.emsp.infrastructure.persistence.mapper.AccountMapper;
import com.emsp.infrastructure.persistence.po.AccountPO;
import com.emsp.infrastructure.persistence.repository.AccountRepositoryImpl;
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
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountRepositoryImplTest {

    @Mock
    private AccountMapper accountMapper;
    
    @Mock
    private AccountConverter accountConverter;
    
    @InjectMocks
    private AccountRepositoryImpl accountRepository;
    
    private Account testAccount;
    private AccountPO testAccountPO;
    
    @BeforeEach
    void setUp() {
        testAccount = Account.create("test@example.com");
        testAccount.setId(1L);
        
        testAccountPO = new AccountPO();
        testAccountPO.setId(1L);
        testAccountPO.setEmail("test@example.com");
        testAccountPO.setStatus(AccountStatus.CREATED);
    }

    @Test
    void save_shouldInsertNewAccount() {
        // 准备
        when(accountConverter.toPO(any(Account.class))).thenReturn(testAccountPO);
        
        // 执行
        Account saved = accountRepository.save(testAccount);
        
        // 验证
        assertNotNull(saved);
        assertEquals(1L, saved.getId());
    }

    @Test
    void findById_shouldReturnAccount() {
        // 准备
        when(accountMapper.selectById(anyLong())).thenReturn(testAccountPO);
        when(accountConverter.toDomain(any(AccountPO.class))).thenReturn(testAccount);
        
        // 执行
        Optional<Account> result = accountRepository.findById(1L);
        
        // 验证
        assertTrue(result.isPresent());
        assertEquals("test@example.com", result.get().getEmail());
    }

    @Test
    void findByEmail_shouldReturnAccount() {
        // 准备
        when(accountMapper.selectByEmail(anyString())).thenReturn(testAccountPO);
        when(accountConverter.toDomain(any(AccountPO.class))).thenReturn(testAccount);
        
        // 执行
        Optional<Account> result = accountRepository.findByEmail("test@example.com");
        
        // 验证
        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
    }

    @Test
    void findByLastUpdatedAfter_shouldReturnList() {
        // 准备
        Instant testTime = Instant.now();
        when(accountMapper.selectByLastUpdatedAfter(any(Instant.class), anyInt(), anyInt()))
            .thenReturn(Collections.singletonList(testAccountPO));
        when(accountConverter.toDomain(any(AccountPO.class))).thenReturn(testAccount);
        
        // 执行
        List<Account> result = accountRepository.findByLastUpdatedAfter(testTime, 0, 10);
        
        // 验证
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("test@example.com", result.get(0).getEmail());
    }
}