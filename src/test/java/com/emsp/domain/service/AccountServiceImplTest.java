package com.emsp.domain.service;

import com.emsp.domain.model.Account;
import com.emsp.domain.model.AccountStatus;
import com.emsp.domain.repository.AccountRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceImplTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountServiceImpl accountService;

    @Test
    void createAccount_shouldSuccess() {
        String email = "test@example.com";
        when(accountRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(accountRepository.save(any(Account.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Account account = accountService.createAccount(email);

        assertNotNull(account);
        assertEquals(email, account.getEmail());
        assertEquals(AccountStatus.CREATED, account.getStatus());
        verify(accountRepository, times(1)).save(any(Account.class));
    }

    @Test
    void createAccount_shouldThrowWhenEmailExists() {
        String email = "test@example.com";
        when(accountRepository.findByEmail(anyString())).thenReturn(Optional.of(new Account("test1@example.com")));

        assertThrows(IllegalArgumentException.class, () -> accountService.createAccount(email));
    }

    @Test
    void changeAccountStatus_shouldActivate() {
        Long accountId = 1L;
        Account account = Account.create("test@example.com");
        when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));
        when(accountRepository.save(any(Account.class))).thenReturn(account);

        accountService.changeAccountStatus(accountId, AccountStatus.ACTIVATED);

        assertEquals(AccountStatus.ACTIVATED, account.getStatus());
        verify(accountRepository, times(1)).save(account);
    }

    @Test
    void changeAccountStatus_shouldThrowWhenAccountNotFound() {
        Long accountId = 1L;
        when(accountRepository.findById(accountId)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, 
            () -> accountService.changeAccountStatus(accountId, AccountStatus.ACTIVATED));
    }
}