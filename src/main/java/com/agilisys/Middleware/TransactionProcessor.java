package com.agilisys.Middleware;

import com.agilisys.Models.Status;
import com.agilisys.Models.Transaction;
import com.agilisys.Services.PaymentsService;

import java.util.Date;

public class TransactionProcessor implements PaymentMiddleware {

    private PaymentMiddleware next;
    private static final PaymentsService paymentsService = PaymentsService.getInstance();

    @Override
    public void setNext(PaymentMiddleware next) {
        this.next = next;
    }

    @Override
    public synchronized void process(Transaction t) {
        /* atomic block */

        if (t.getStatus() == Status.ACCEPTED) {
            /*only ACCEPTED transactions are processed*/

            paymentsService.processPayment(t);
            t.setEndDate(new Date());
            t.setMessage("Transaction succesfully completed");
            t.setStatus(Status.COMPLETED);
            logger.info("Transaction " + t.getId() + " changed status to " + t.getStatus().toString());
            paymentsService.saveTransaction(t);
            logger.info("Transaction " + t.getId() + " saved to db");
        }

    }
}
