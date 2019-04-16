package com.betpawa.wallet;

import com.betpawa.wallet.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WalletApplication {

    public static final String API_V1 = "api";

    @Autowired
    UserRepository repository;

    public static void main(String[] args) {
        SpringApplication.run(WalletApplication.class, args);
    }
}
