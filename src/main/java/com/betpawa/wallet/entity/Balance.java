package com.betpawa.wallet.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name = "balance")
public class Balance {

    public Balance() {

    }

    public Balance(Long userId, BigDecimal amount, Long currencyId) {
        this.userId = userId;
        this.amount = amount;
        this.currencyId = currencyId;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private Long userId;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @OneToOne
    @JoinColumn(name = "currency_id")
    private Long currencyId;

    public Long getUserId() {
        return userId;
    }

    public Balance setUserId(Long userId) {
        this.userId = userId;
        return this;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public Balance setAmount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    public Long getCurrencyId() {
        return currencyId;
    }

    public Balance setCurrencyId(Long currencyId) {
        this.currencyId = currencyId;
        return this;
    }
}
