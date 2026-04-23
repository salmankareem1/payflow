package com.salman.payflow.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;

import com.salman.payflow.model.Transaction;
import com.salman.payflow.model.Wallet;
import com.salman.payflow.repository.TransactionRepository;
import com.salman.payflow.repository.WalletRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class TransactionService {
	//private final Transaction transaction;
	                         
	private final TransactionRepository transactionRepository;
	private final WalletRepository walletRepository;
	
	public TransactionService(TransactionRepository transactionRepository, WalletRepository walletrepository) {
		this.transactionRepository=transactionRepository;
		this.walletRepository=walletrepository;
	}

	public void transfer(long fromId,long toId, BigDecimal amount) {
		
		int maxRetries=3;
		int attempt =0;
		
		while(attempt<maxRetries) {
		
		try {
		
		String referenceId="TXN-"+System.currentTimeMillis();
		
		if(fromId==toId) {
			throw new RuntimeException("Sender and receiver cannot be same");
		}
		
		Wallet from = walletRepository.findById(fromId).orElseThrow(()->new RuntimeException("The Sender not found"));	
		Wallet to = walletRepository.findById(toId).orElseThrow(()->new RuntimeException("The Receiver not found"));	
		
		if (!from.getCurrency().equals(to.getCurrency())) {
		    throw new RuntimeException("Transfer between different currencies is not allowed");
		}
		
		if(from.getBalance().compareTo(amount)<0) {
			throw new RuntimeException("Insufficient Balance");
		}
		
		if (amount.compareTo(BigDecimal.ZERO) <= 0) {
		    throw new RuntimeException("Amount must be greater than zero");
		}
		
		from.setBalance((from.getBalance().subtract(amount)));
		to.setBalance(to.getBalance().add(amount));
		
		walletRepository.save(from);
		walletRepository.save(to);
		
		
		Transaction tx= new Transaction();
		tx.setFromWalletId(fromId);
		tx.setToWalletId(toId);
		tx.setAmount(amount);
		tx.setStatus("Success");
		tx.setReferenceId(referenceId);
		
		transactionRepository.save(tx);
			
	}catch (ObjectOptimisticLockingFailureException e) {
		attempt++;
	
		if (attempt >= maxRetries) {
            throw new RuntimeException("Transaction failed due to concurrent updates. Please retry");
        }

        try {
            Thread.sleep(100); // small delay before retry
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
	
		}
		
		}
		throw new RuntimeException("Unexpected failure");
	}	
	public List<Transaction>getAllTransactions(){
		return transactionRepository.findAll();
	}
	
	public List<Transaction> getTransactionsByWalletId(long walletId) {
	    return transactionRepository
	        .findByFromWalletIdOrToWalletId(walletId, walletId);
	}
}
