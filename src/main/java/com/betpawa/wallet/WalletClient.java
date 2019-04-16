package com.betpawa.wallet;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import com.betpawa.wallet.WalletGrpc.WalletBlockingStub;
import com.betpawa.wallet.WalletGrpc.WalletStub;
import io.grpc.StatusRuntimeException;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

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
    public void makeDeposit(Long user, String amount, CURRENCY currency) {
        info("*** makeDeposit: user={0} lon={1} cur={2}", user, amount, currency);
        WalletRequest request = WalletRequest.newBuilder()
            .setUser(user)
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
    public void makeWithdrawal(Long user, String amount, CURRENCY currency) {
        info("*** makeWithdrawal: userId={0} lon={1} cur={2}", user, amount, currency);
        WalletRequest request = WalletRequest.newBuilder()
                .setUser(user)
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

    public BalanceSummary getBalance(Long user) {
        info("*** getBalance: userId={0}", user);
        BalanceSummary summary = null;
        WalletRequest request = WalletRequest.newBuilder().setUser(user).build();

        try {
            summary = blockingStub.balance(request);
        }
        catch (StatusRuntimeException e) {
            warning("RPC failed: {0}", e.getStatus());
        }
        return summary;
    }

    public void registerUser(Long id) {
        info("*** registerUser: user={0}", id);
        UserRequest request = UserRequest.newBuilder().setId(id).build();

        try {
            blockingStub.register(request);
        }
        catch (StatusRuntimeException e) {
            warning("RPC failed: {0}", e.getStatus());
        }
    }

    /**
     * The main method performs various requests to the server and then exits.
     * @param args
     * @throws InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {
        Options options = new Options();
        WalletClient client = new WalletClient("localhost", 6565);

        Option usersOption = new Option("u", "users", true, "number of concurrent users emulated");
        usersOption.setRequired(true);
        options.addOption(usersOption);

        Option threadsOption = new Option("t", "threads", true, "number of concurrent requests a user will make");
        threadsOption.setRequired(true);
        options.addOption(threadsOption);

        Option roundsOption = new Option("r", "rounds", true, "number of rounds each thread is executing");
        roundsOption.setRequired(true);
        options.addOption(roundsOption);

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd;

        try {
            cmd = parser.parse(options, args);
            String users = cmd.getOptionValue("users");
            String threads = cmd.getOptionValue("threads");
            String rounds = cmd.getOptionValue("rounds");

            // Register users in DB
            for(long i = 1; i <= Long.parseLong(users); i++) {
                client.registerUser(i);
            }

            // Perform wallet operations
            client.makeWithdrawal(1L, "200", CURRENCY.USD);
            client.makeDeposit(1L, "100", CURRENCY.USD);
            client.getBalance(1L);
            client.makeWithdrawal(1L, "200", CURRENCY.USD);
            client.makeDeposit(1L, "100", CURRENCY.EUR);
            client.getBalance(1L);
            client.makeWithdrawal(1L, "200", CURRENCY.USD);
            client.makeDeposit(1L, "100", CURRENCY.USD);
            client.getBalance(1L);
            client.makeWithdrawal(1L, "200", CURRENCY.USD);
            client.getBalance(1L);
            client.makeWithdrawal(1L, "200", CURRENCY.USD);

        } catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp("utility-name", options);
            System.exit(1);
        } finally {
            client.shutdown();
        }
    }

    public void rounds(int round) {
        switch (round) {
            case ROUND.A_VALUE:
                break;
            case ROUND.B_VALUE:
                break;
            case ROUND.C_VALUE:
                break;
        }
    }

    public void roundA(long userId, WalletClient client) {
        // Deposit 100 USD
        client.makeDeposit(userId, "100", CURRENCY.USD);

        // Withdraw 200 USD
        client.makeWithdrawal(userId, "200", CURRENCY.USD);

        // Deposit 100 EUR
        client.makeDeposit(userId, "100", CURRENCY.EUR);

        // Get balance
        client.getBalance(userId);

        // Withdraw 100 USD
        client.makeWithdrawal(userId, "100", CURRENCY.USD);

        // Get balance
        client.getBalance(userId);

        // Withdraw 100 USD
        client.makeWithdrawal(userId, "100", CURRENCY.USD);
    }

    public void roundB(long userId, WalletClient client) {
        // Withdraw 100 GBP
        client.makeWithdrawal(userId, "100", CURRENCY.GBP);

        // Deposit 300 GBP
        client.makeDeposit(userId, "300", CURRENCY.GBP);

        // Withdraw 100 GBP
        client.makeWithdrawal(userId, "100", CURRENCY.GBP);

        // Withdraw 100 GBP
        client.makeWithdrawal(userId, "100", CURRENCY.GBP);

        // Withdraw 100 GBP
        client.makeWithdrawal(userId, "100", CURRENCY.GBP);
    }

    public void roundC(long userId, WalletClient client) {
        // Get balance
        client.getBalance(userId);

        // Deposit 100 USD
        client.makeDeposit(userId, "100", CURRENCY.USD);

        // Deposit 100 USD
        client.makeDeposit(userId, "100", CURRENCY.USD);

        // Withdraw 100 USD
        client.makeWithdrawal(userId, "100", CURRENCY.USD);

        // Deposit 100 USD
        client.makeDeposit(userId, "100", CURRENCY.USD);

        // Get balance
        client.getBalance(userId);

        // Withdraw 200 USD
        client.makeWithdrawal(userId, "200", CURRENCY.USD);

        // Get balance
        client.getBalance(userId);

    }

    private void info(String msg, Object... params) {
        logger.log(Level.INFO, msg, params);
    }

    private void warning(String msg, Object... params) {
        logger.log(Level.WARNING, msg, params);
    }
}
