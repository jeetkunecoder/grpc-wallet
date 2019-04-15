package com.betpawa.wallet;

import com.betpawa.wallet.entity.User;
import com.betpawa.wallet.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;

@SpringBootApplication
public class WalletApplication {

    private static final Logger log = LoggerFactory.getLogger(WalletApplication.class);
    public static final String API_V1 = "api";

    @Autowired
    UserRepository repository;

    public static void main(String[] args) {
        SpringApplication.run(WalletApplication.class, args);
    }

//    @Bean
//    public CommandLineRunner buildData() {
//        return (args) -> {
//            repository.save(new User("Mamparito"));
//            log.info("Initialized with users ");
//        };
//    }
}
