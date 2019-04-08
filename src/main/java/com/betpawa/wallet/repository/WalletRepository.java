package com.betpawa.wallet.repository;

import com.betpawa.wallet.entity.Balance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WalletRepository extends JpaRepository<Balance, Long> {

}
