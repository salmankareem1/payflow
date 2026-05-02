package com.salman.payflow.service;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

import com.salman.payflow.dto.CreateWalletRequest;
import com.salman.payflow.exception.WalletNotFoundException;
import com.salman.payflow.model.Wallet;
import com.salman.payflow.repository.WalletRepository;


@Service
public class WalletService {

	private final WalletRepository walletRepository;
	
	public WalletService(WalletRepository walletRepository) {
		this.walletRepository = walletRepository;
		
	}
	
	public Wallet createWallet(CreateWalletRequest createWalletRequest) {
		Wallet wallet = new Wallet();
		wallet.setUserId(createWalletRequest.getUserId());
		wallet.setCurrency(createWalletRequest.getCurrency());
		wallet.setBalance(BigDecimal.ZERO);
		return walletRepository.save(wallet);
		
	}
	
	public List<Wallet> getAllWallets(){
		return walletRepository.findAll();
	}
	@Cacheable(value = "wallets", key = "#id")
	public Wallet getWalletById(long id) {
		return walletRepository.findById(id).orElseThrow(()-> new WalletNotFoundException(id));
	}
	@CacheEvict(value = "wallets", key = "#id")
	public Wallet updateWallet(long id, Wallet updatedWallet) {
		Wallet wallet= walletRepository.findById(id).orElseThrow(()-> new WalletNotFoundException(id));
		
			wallet.setUserId(updatedWallet.getUserId());
			wallet.setBalance(updatedWallet.getBalance());
			wallet.setCurrency(updatedWallet.getCurrency());
			return walletRepository.save(wallet);
			
	}
	@CacheEvict(value = "wallets", key = "#id")
	public void deleteWallet(long id)
	{
		Wallet wallet = walletRepository.findById(id).orElseThrow(()-> new WalletNotFoundException(id));
				walletRepository.delete(wallet);
	}
	
}
