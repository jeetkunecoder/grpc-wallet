package com.betpawa.wallet;

import com.betpawa.wallet.entity.Balance;
import com.betpawa.wallet.repository.WalletRepository;
import com.google.protobuf.Empty;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;

import java.io.IOException;
import java.math.BigDecimal;

import org.apache.log4j.BasicConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class WalletServer {
    private static final Logger logger = LoggerFactory.getLogger(WalletServer.class);

    private final int port;
    private final Server server;

    /** Create a WalletServer listening on {@code port} */
    public WalletServer(int port) {
        this(ServerBuilder.forPort(port), port);
    }

    /** Create a WalletServer using serverBuilder as a base. */
    public WalletServer(ServerBuilder<?> serverBuilder, int port) {
        this.port = port;
        server = serverBuilder.addService(new WalletService()).build();
    }

    /** Start serving requests. */
    public void start() throws IOException {
        server.start();
        logger.info("Server started, listening on " + port);
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                // Use stderr here since the logger may has been reset by its JVM shutdown hook.
                System.err.println("*** shutting down gRPC server since JVM is shutting down");
                WalletServer.this.stop();
                System.err.println("*** server shut down");
            }
        });
    }

    /** Stop serving requests and shutdown resources. */
    public void stop() {
        if (server != null) {
            server.shutdown();
        }
    }

    /**
     * Await termination on the main thread since the grpc library uses daemon threads.
     */
    private void blockUntilShutdown() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }

    /**
     * Main method.
     */
    public static void main(String[] args) throws Exception {
        BasicConfigurator.configure();
        WalletServer server = new WalletServer(8980);
        server.start();
        server.blockUntilShutdown();
    }


    private static class WalletService extends WalletGrpc.WalletImplBase {
        @Autowired
        private WalletRepository repository;

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

        private Empty makeDeposit(WalletRequest request) {
            logger.info("Requesting deposit transaction of " +
                    request.getCurrency() + " " + request.getAmount());

//            Balance balance = new Balance()
//                    .setUserId(request.getUserId())
//                    .setAmount(new BigDecimal(request.getAmount()))
//                    .setCurrencyId(request.getCurrency());

            //repository.save(balance);
            return Empty.getDefaultInstance();
        }

        private Empty makeWithdrawal(WalletRequest request) {
            logger.info("Requesting withdrawal transaction...");
            return Empty.getDefaultInstance();
        }

        private BalanceSummary getBalance(WalletRequest request) {
            logger.info("Obtaining account balance..");
            repository.findById(request.getUserId());

            return BalanceSummary.newBuilder().build();
        }
    }
}
