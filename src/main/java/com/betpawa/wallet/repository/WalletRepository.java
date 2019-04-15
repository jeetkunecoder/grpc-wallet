package com.betpawa.wallet.repository;

import com.betpawa.wallet.entity.Balance;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

public interface WalletRepository extends CrudRepository<Balance, Long> {

    @Transactional(readOnly = true)
    @Query("select sum(b.amount) from Balance b where b.userId=:userId and b.currencyId=:currencyId")
    BigDecimal getUserBalance(@Param("userId") Long userId, @Param("currencyId") Long currencyId);
}