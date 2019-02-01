package com.agilisys.Middleware;
import com.agilisys.Models.Transaction;
import com.agilisys.Services.BusinessService;

import java.util.logging.Logger;

public interface PaymentMiddleware {
    /* Implementing allows additional procedures in Payment chain functionality */

    BusinessService businessService = BusinessService.getInstance();
    Logger logger = Logger.getLogger("MoneyTransactionAPI");
    PaymentMiddleware next = null;

    void setNext(PaymentMiddleware next);
    void process(Transaction t);
}
