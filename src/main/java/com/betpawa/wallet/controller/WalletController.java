package com.betpawa.wallet.controller;

import com.betpawa.wallet.WalletApplication;
import com.betpawa.wallet.WalletRequest;
import com.betpawa.wallet.service.WalletService;
import com.google.protobuf.Empty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = WalletApplication.API_V1, produces = MediaType.APPLICATION_JSON_VALUE)
public class WalletController {

    public static final String ENDPOINT = "wallet";

    @Autowired
    WalletService service;

    @RequestMapping(value = ENDPOINT, method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public HttpEntity<Empty> registerUser(final @Validated @RequestBody WalletRequest request) {
        return new ResponseEntity<>(service.makeDeposit(request), HttpStatus.OK);
    }
}
