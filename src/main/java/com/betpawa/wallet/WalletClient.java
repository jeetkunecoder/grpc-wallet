package com.betpawa.wallet;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import com.betpawa.wallet.WalletGrpc.WalletBlockingStub;
import com.betpawa.wallet.WalletGrpc.WalletStub;
import io.grpc.StatusRuntimeException;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WalletClient {
    private static final Logger logger = Logger.getLogger(WalletClient.class.getName());

    private final ManagedChannel channel;
    private final WalletBlockingStub blockingStub;
    private final WalletStub asyncStub;

    private Random random = new Random();

    /** Construct client for accessing the WalletServer at {@code host:port}. */
    public WalletClient(String host, int port) {
        this(ManagedChannelBuilder.forAddress(host, port).usePlaintext());
    }

    /** Construct client for accessing the WalletServer using the existing channel. */
    public WalletClient(ManagedChannelBuilder<?> channelBuilder) {
        channel = channelBuilder.build();
        blockingStub = WalletGrpc.newBlockingStub(channel);
        asyncStub = WalletGrpc.newStub(channel);
    }

    public void shutdown() throws InterruptedException {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
    }

    /**
     * Request a deposit transaction
     */
    public void makeDeposit(int userId, String amount, CURRENCY currency) {
        info("*** makeDeposit: userId={0} lon={1} cur={2}", userId, amount, currency);
        WalletRequest request = WalletRequest.newBuilder()
            .setUserId(userId)
            .setAmount(amount)
            .setCurrency(currency)
            .build();

        try {
            blockingStub.deposit(request);
        }
        catch (StatusRuntimeException e) {
            warning("RPC failed: {0}", e.getStatus());
            return;
        }
    }

    /**
     * Request a withdrawal transaction
     */
    public void makeWithdrawal(int userId, String amount, CURRENCY currency) {
        info("*** makeWithdrawal: userId={0} lon={1} cur={2}", userId, amount, currency);
        WalletRequest request = WalletRequest.newBuilder()
                .setUserId(userId)
                .setAmount(amount)
                .setCurrency(currency)
                .build();

        try {
            blockingStub.withdraw(request);
        }
        catch (StatusRuntimeException e) {
            warning("RPC failed: {0}", e.getStatus());
            return;
        }
    }

    // TODO: Handle null value scenario
    public BalanceSummary getBalance(int userId) {
        info("*** getBalance: userId={0}", userId);
        BalanceSummary summary = null;
        WalletRequest request = WalletRequest.newBuilder().setUserId(userId).build();

        try {
            summary = blockingStub.balance(request);
        }
        catch (StatusRuntimeException e) {
            warning("RPC failed: {0}", e.getStatus());
        }
        return summary;
    }


    /**
     * The main method performs various requests to the server and then exits.
     * @param args
     * @throws InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {

        WalletClient client = new WalletClient("localhost", 8980);
        try {
            // ROUND A
            // Deposit 100 USD
            client.makeDeposit(1, "100", CURRENCY.USD);

            // Withdraw 200 USD
            client.makeWithdrawal(1, "200", CURRENCY.USD);

            // Deposit 100 EUR
            client.makeDeposit(1, "100", CURRENCY.EUR);

            // Get balance
            client.getBalance(1);

            // Withdraw 100 USD
            client.makeWithdrawal(1, "100", CURRENCY.USD);

            // Get balance
            client.getBalance(1);

            // Withdraw 100 USD
            client.makeWithdrawal(1, "100", CURRENCY.USD);

        } finally {
            client.shutdown();
        }
    }

    private void info(String msg, Object... params) {
        logger.log(Level.INFO, msg, params);
    }

    private void warning(String msg, Object... params) {
        logger.log(Level.WARNING, msg, params);
    }
}
