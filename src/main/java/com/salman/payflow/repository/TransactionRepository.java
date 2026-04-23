package com.salman.payflow.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.salman.payflow.model.Transaction;


public interface TransactionRepository extends JpaRepository<Transaction,Long>{

	 List<Transaction> findByFromWalletIdOrToWalletId(long fromId, long toId);
}