package com.salman.payflow.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.salman.payflow.model.Wallet;

public interface WalletRepository extends JpaRepository<Wallet,Long>{

}
