package com.salman.payflow;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.salman.payflow.exception.WalletNotFoundException;
import com.salman.payflow.model.Wallet;
import com.salman.payflow.repository.WalletRepository;
import com.salman.payflow.service.WalletService;

@ExtendWith(MockitoExtension.class)
class WalletServiceTest {

    @Mock
    private WalletRepository walletRepository;

    private WalletService walletService;

    @BeforeEach
    void setUp() {
        walletService = new WalletService(walletRepository);
    }

    private Wallet buildWallet(Long id, String userId, String currency, BigDecimal balance) {
        Wallet wallet = new Wallet();
        wallet.setId(id);
        wallet.setUserId(userId);
        wallet.setCurrency(currency);
        wallet.setBalance(balance);
        wallet.setVersion(0L);
        return wallet;
    }

    @Test
    void getWalletById_exists_returnsWallet() {
        Wallet wallet = buildWallet(1L, "user1", "EUR", new BigDecimal("500.00"));
        when(walletRepository.findById(1L)).thenReturn(Optional.of(wallet));

        Wallet result = walletService.getWalletById(1L);

        assertEquals(1L, result.getId());
        assertEquals("user1", result.getUserId());
        assertEquals("EUR", result.getCurrency());
    }

    @Test
    void getWalletById_notFound_throwsWalletNotFoundException() {
        when(walletRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(WalletNotFoundException.class,
            () -> walletService.getWalletById(99L));
    }

    @Test
    void getAllWallets_returnsAllWallets() {
        List<Wallet> wallets = List.of(
            buildWallet(1L, "user1", "EUR", new BigDecimal("500.00")),
            buildWallet(2L, "user2", "USD", new BigDecimal("1000.00"))
        );
        when(walletRepository.findAll()).thenReturn(wallets);

        List<Wallet> result = walletService.getAllWallets();

        assertEquals(2, result.size());
        verify(walletRepository, times(1)).findAll();
    }

    @Test
    void deleteWallet_exists_deletesSuccessfully() {
        Wallet wallet = buildWallet(1L, "user1", "EUR", new BigDecimal("500.00"));
        when(walletRepository.findById(1L)).thenReturn(Optional.of(wallet));

        walletService.deleteWallet(1L);

        verify(walletRepository, times(1)).delete(wallet);
    }

    @Test
    void deleteWallet_notFound_throwsWalletNotFoundException() {
        when(walletRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(WalletNotFoundException.class,
            () -> walletService.deleteWallet(99L));

        verify(walletRepository, never()).delete(any());
    }

    @Test
    void updateWallet_notFound_throwsWalletNotFoundException() {
        when(walletRepository.findById(99L)).thenReturn(Optional.empty());

        Wallet updatedDetails = buildWallet(99L, "user1", "EUR", new BigDecimal("500.00"));

        assertThrows(WalletNotFoundException.class,
            () -> walletService.updateWallet(99L, updatedDetails));

        verify(walletRepository, never()).save(any());
    }

    @Test
    void updateWallet_exists_updatesCorrectly() {
        Wallet existing = buildWallet(1L, "user1", "EUR", new BigDecimal("500.00"));
        Wallet updatedDetails = buildWallet(1L, "user1Updated", "EUR", new BigDecimal("500.00"));

        when(walletRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(walletRepository.save(any(Wallet.class))).thenAnswer(i -> i.getArgument(0));

        Wallet result = walletService.updateWallet(1L, updatedDetails);

        assertEquals("user1Updated", result.getUserId());
        verify(walletRepository, times(1)).save(existing);
    }
}