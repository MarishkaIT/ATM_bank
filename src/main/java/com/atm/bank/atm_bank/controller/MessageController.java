package com.atm.bank.atm_bank.controller;

import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Locale;

@RestController
public class MessageController {

    private MessageSource messageSource;

    @GetMapping("/welcome")
    public String welcome(@RequestParam(value = "lang", defaultValue = "en") String lang) {
        Locale locale = new Locale(lang);
        return messageSource.getMessage("welcome.message", null, locale);
    }

    @GetMapping("/transaction/success")
    public String transactionSuccess(@RequestParam(value = "lang", defaultValue = "en") String lang) {
        Locale locale = new Locale(lang);
        return messageSource.getMessage("transaction.success", null, locale);
    }
    @GetMapping("/transaction/failure")
    public String transactionFail(@RequestParam(value = "lang", defaultValue = "en") String lang) {
        Locale locale = new Locale(lang);
        return messageSource.getMessage("transaction.failure", null, locale);
    }
}
