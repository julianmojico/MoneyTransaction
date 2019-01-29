package com.agilisys.Middleware;

import com.agilisys.Models.Transaction;
import com.agilisys.Services.InMemoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface PaymentMiddleware {
    /* Implementing allows additional procedures in Payment chain functionality */

    Logger logger = LoggerFactory.getLogger(PaymentMiddleware.class);
    PaymentMiddleware next = null;
    InMemoryRepository repository = null;

    void setNext(PaymentMiddleware next);

    void process(Transaction t);
}
