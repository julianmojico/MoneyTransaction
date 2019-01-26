package com.agilisys.Middleware;

import com.agilisys.Models.Transaction;
import com.agilisys.Services.AccountService;
import com.agilisys.Services.InMemoryRepository;

public interface PaymentMiddleware {

    PaymentMiddleware next = null;
    InMemoryRepository repository = null;

    AccountService accountService = AccountService.getInstance();
    void setNext(PaymentMiddleware next);

    void process(Transaction t) throws Exception;
}
