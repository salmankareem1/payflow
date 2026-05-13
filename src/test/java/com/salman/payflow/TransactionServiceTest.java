package com.salman.payflow;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.salman.payflow.exception.CurrencyMismatchException;
import com.salman.payflow.exception.InsufficientFundsException;
import com.salman.payflow.exception.SameWalletException;
import com.salman.payflow.exception.WalletNotFoundException;
import com.salman.payflow.model.Transaction;
import com.salman.payflow.model.Wallet;
import com.salman.payflow.repository.TransactionRepository;
import com.salman.payflow.repository.WalletRepository;
import com.salman.payflow.service.TransactionService;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private WalletRepository walletRepository;

    @Mock
    private TransactionRepository transactionRepository;

    private TransactionService transactionService;

    @BeforeEach
    void setUp() {
        transactionService = new TransactionService(transactionRepository, walletRepository);
    }

    // Helper method to build a wallet quickly
    private Wallet buildWallet(Long id, String currency, BigDecimal balance) {
        Wallet wallet = new Wallet();
        wallet.setId(id);
        wallet.setCurrency(currency);
        wallet.setBalance(balance);
        wallet.setUserId("testUser");
        wallet.setVersion(0L);
        return wallet;
    }

    // ─── HAPPY PATH ───────────────────────────────────────────────────────────

    @Test
    void transfer_happyPath_balancesUpdatedCorrectly() {
        // Arrange
        Wallet from = buildWallet(1L, "EUR", new BigDecimal("500.00"));
        Wallet to   = buildWallet(2L, "EUR", new BigDecimal("100.00"));

        when(walletRepository.findById(1L)).thenReturn(Optional.of(from));
        when(walletRepository.findById(2L)).thenReturn(Optional.of(to));
        when(walletRepository.save(any(Wallet.class))).thenAnswer(i -> i.getArgument(0));
        when(transactionRepository.save(any(Transaction.class))).thenAnswer(i -> i.getArgument(0));

        // Act
        transactionService.transfer(1L, 2L, new BigDecimal("200.00"));

        // Assert — sender debited, receiver credited
        assertEquals(new BigDecimal("300.00"), from.getBalance());
        assertEquals(new BigDecimal("300.00"), to.getBalance());

        // Verify both wallets were saved and transaction was recorded
        verify(walletRepository, times(2)).save(any(Wallet.class));
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    // ─── VALIDATION ───────────────────────────────────────────────────────────

    @Test
    void transfer_sameWallet_throwsSameWalletException() {
        // Act & Assert
        assertThrows(SameWalletException.class,
            () -> transactionService.transfer(1L, 1L, new BigDecimal("100.00")));

        // Verify no database calls were made
        verifyNoInteractions(walletRepository);
        verifyNoInteractions(transactionRepository);
    }

    @Test
    void transfer_nullAmount_throwsRuntimeException() {
        assertThrows(RuntimeException.class,
            () -> transactionService.transfer(1L, 2L, null));

        verifyNoInteractions(walletRepository);
    }

    @Test
    void transfer_zeroAmount_throwsRuntimeException() {
        assertThrows(RuntimeException.class,
            () -> transactionService.transfer(1L, 2L, BigDecimal.ZERO));

        verifyNoInteractions(walletRepository);
    }

    @Test
    void transfer_negativeAmount_throwsRuntimeException() {
        assertThrows(RuntimeException.class,
            () -> transactionService.transfer(1L, 2L, new BigDecimal("-100.00")));

        verifyNoInteractions(walletRepository);
    }

    // ─── WALLET NOT FOUND ─────────────────────────────────────────────────────

    @Test
    void transfer_senderNotFound_throwsWalletNotFoundException() {
        when(walletRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(WalletNotFoundException.class,
            () -> transactionService.transfer(1L, 2L, new BigDecimal("100.00")));

        verify(transactionRepository, never()).save(any());
    }

    @Test
    void transfer_receiverNotFound_throwsWalletNotFoundException() {
        Wallet from = buildWallet(1L, "EUR", new BigDecimal("500.00"));
        when(walletRepository.findById(1L)).thenReturn(Optional.of(from));
        when(walletRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(WalletNotFoundException.class,
            () -> transactionService.transfer(1L, 2L, new BigDecimal("100.00")));

        verify(transactionRepository, never()).save(any());
    }

    // ─── BUSINESS RULES ───────────────────────────────────────────────────────

    @Test
    void transfer_insufficientFunds_throwsInsufficientFundsException() {
        Wallet from = buildWallet(1L, "EUR", new BigDecimal("50.00"));
        Wallet to   = buildWallet(2L, "EUR", new BigDecimal("100.00"));

        when(walletRepository.findById(1L)).thenReturn(Optional.of(from));
        when(walletRepository.findById(2L)).thenReturn(Optional.of(to));

        assertThrows(InsufficientFundsException.class,
            () -> transactionService.transfer(1L, 2L, new BigDecimal("100.00")));

        // Verify no saves happened — balances must not be touched
        verify(walletRepository, never()).save(any());
        verify(transactionRepository, never()).save(any());
    }

    @Test
    void transfer_currencyMismatch_throwsCurrencyMismatchException() {
        Wallet from = buildWallet(1L, "EUR", new BigDecimal("500.00"));
        Wallet to   = buildWallet(2L, "USD", new BigDecimal("100.00"));

        when(walletRepository.findById(1L)).thenReturn(Optional.of(from));
        when(walletRepository.findById(2L)).thenReturn(Optional.of(to));

        assertThrows(CurrencyMismatchException.class,
            () -> transactionService.transfer(1L, 2L, new BigDecimal("100.00")));

        verify(walletRepository, never()).save(any());
        verify(transactionRepository, never()).save(any());
    }

    @Test
    void transfer_exactBalance_succeeds() {
        // Edge case — transferring exactly the available balance
        Wallet from = buildWallet(1L, "EUR", new BigDecimal("100.00"));
        Wallet to   = buildWallet(2L, "EUR", new BigDecimal("0.00"));

        when(walletRepository.findById(1L)).thenReturn(Optional.of(from));
        when(walletRepository.findById(2L)).thenReturn(Optional.of(to));
        when(walletRepository.save(any(Wallet.class))).thenAnswer(i -> i.getArgument(0));
        when(transactionRepository.save(any(Transaction.class))).thenAnswer(i -> i.getArgument(0));

        // Should not throw
        assertDoesNotThrow(
            () -> transactionService.transfer(1L, 2L, new BigDecimal("100.00")));

        assertEquals(BigDecimal.ZERO.setScale(2), from.getBalance().setScale(2));
        assertEquals(new BigDecimal("100.00"), to.getBalance());
    }
}