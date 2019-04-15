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
import java.util.Date;

@Entity
@Table(name = "balance")
public class Balance {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Column(name = "currency_id", nullable = false)
    private Long currencyId;

    @Column(name = "created_at", nullable = false)
    private Date createdAt;

    @Column(name = "operation_type", nullable = false)
    private int operationType;

    public Balance() {

    }

    public Balance(Long userId, BigDecimal amount, Long currencyId, Date createdAt, Byte operationType) {
        this.userId = userId;
        this.amount = amount;
        this.currencyId = currencyId;
        this.createdAt = createdAt;
        this.operationType = operationType;
    }

    public Long getUser() {
        return userId;
    }

    public Balance setUser(Long user) {
        this.userId = user;
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

    public Date getCreatedAt() {
        return createdAt;
    }

    public Balance setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public int getOperationType() {
        return operationType;
    }

    public Balance setOperationType(int operationType) {
        this.operationType = operationType;
        return this;
    }

    @Override
    public String toString() {
        return "Balance{" +
                "id=" + id +
                ", user_id='" + userId + '\'' +
                ", amount='" + amount + '\'' +
                ", currency_id='" + currencyId + '\'' +
                ", created_at='" + createdAt + '\'' +
                ", operation_type='" + operationType + '\'' +
                '}';
    }
}
