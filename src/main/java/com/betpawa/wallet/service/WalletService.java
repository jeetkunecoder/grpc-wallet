package com.betpawa.wallet.service;

import com.betpawa.wallet.BalanceSummary;
import com.betpawa.wallet.CURRENCY;
import com.betpawa.wallet.OPERATION_TYPE;
import com.betpawa.wallet.WalletGrpc;
import com.betpawa.wallet.WalletRequest;

import com.betpawa.wallet.entity.Balance;
import com.betpawa.wallet.entity.User;
import com.betpawa.wallet.repository.UserRepository;
import com.betpawa.wallet.repository.WalletRepository;
import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;
import org.lognet.springboot.grpc.GRpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.Date;

@GRpcService
public class WalletService extends WalletGrpc.WalletImplBase {

    public static final Logger logger = LoggerFactory.getLogger(WalletService.class);
    @Autowired
    private WalletRepository walletRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public void deposit(WalletRequest request, StreamObserver<Empty> responseObserver) {
        responseObserver.onNext(makeDeposit(request));
        responseObserver.onCompleted();
    }

    @Override
    public void withdraw(WalletRequest request, StreamObserver<Empty> responseObserver) {
        responseObserver.onNext(makeWithdrawal(request));
        responseObserver.onCompleted();
    }

    @Override
    public void balance(WalletRequest request, StreamObserver<BalanceSummary> responseObserver) {
        responseObserver.onNext(getBalance(request));
        responseObserver.onCompleted();
    }

    private void createUser() {

    }

    public Empty makeDeposit(WalletRequest request) {
        logger.info("Requesting deposit transaction of " +
                request.getCurrency() + " " + request.getAmount());

        User user = new User(request.getUser(), "user_" + request.getUser());

        logger.info("Registering: " + user.getName());
        userRepository.save(user);

        logger.info("Depositing: " + request.getAmount() + " " + request.getCurrency());

        Balance balance = new Balance()
            .setUser(request.getUser())
            .setAmount(new BigDecimal(request.getAmount()))
            .setCurrencyId((long)request.getCurrency().getNumber())
            .setCreatedAt(new Date())
            .setOperationType(OPERATION_TYPE.TRANSACTION_VALUE);

        walletRepository.save(balance);
        return Empty.getDefaultInstance();
    }

    private Empty makeWithdrawal(WalletRequest request) {
        logger.info("Requesting withdrawal transaction of " +
            request.getCurrency() + " " + request.getAmount());
        BigDecimal requestedAmount = new BigDecimal(request.getAmount());
        BigDecimal balanceAmount;
        if(request.getCurrency() != CURRENCY.UNRECOGNIZED) {
            logger.info("Unknown currency: " + request.getCurrency());
            return Empty.getDefaultInstance();
        }
        balanceAmount = walletRepository.getUserBalance(request.getUser(), (long)request.getCurrency().getNumber());
        if(balanceAmount != null && fundsAreSufficient(requestedAmount, balanceAmount)) {
            Balance balance = new Balance()
                .setUser(request.getUser())
                .setAmount(new BigDecimal("-" + request.getAmount()))
                .setCurrencyId((long)request.getCurrency().getNumber())
                .setCreatedAt(new Date())
                .setOperationType(OPERATION_TYPE.TRANSACTION_VALUE);
            walletRepository.save(balance);
            logger.info("Request OK.");
        }
        else {
            logger.info("Insufficient funds.");
        }
        return Empty.getDefaultInstance();
    }

    private BalanceSummary getBalance(WalletRequest request) {
        logger.info("Obtaining account balance..");
        BalanceSummary summary = BalanceSummary.getDefaultInstance();
        for(CURRENCY currency : CURRENCY.values()) {
            BigDecimal amount = null;
            if(currency != CURRENCY.UNRECOGNIZED) {
                amount = walletRepository.getUserBalance(request.getUser(), (long)currency.getNumber());
            }
            if(amount != null) {
                summary.toBuilder().putAmount(amount.toString(), currency);
                logger.info("User ID: " + request.getUser() +
                    " - Account balance in " + currency + " is: " + amount.toString());
            }
        }
        return summary;
    }

    private boolean fundsAreSufficient(BigDecimal request, BigDecimal balance) {
        return request.compareTo(balance) == 0 || request.compareTo(balance) == -1;
    }
}
