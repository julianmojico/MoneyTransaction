package com.agilisys.Middleware;

import com.agilisys.Models.Account;
import com.agilisys.Models.Status;
import com.agilisys.Models.Transaction;

import java.util.Date;

public class TransactionProcessor implements PaymentMiddleware {

    private PaymentMiddleware next;

    @Override
    public void setNext(PaymentMiddleware next) {
        this.next = next;
    }

    @Override
    public synchronized void process(Transaction t) {
        /* atomic block */

        if (t.getStatus() == Status.ACCEPTED) {
            /*only ACCEPTED transactions are processed*/

            processPayment(t);
            t.setEndDate(new Date());
            t.setMessage("Transaction succesfully completed");
            t.setStatus(Status.COMPLETED);
            logger.info("Transaction " + t.getId() + " changed status to " + t.getStatus().toString());
            businessService.saveTransaction(t);
            logger.info("Transaction " + t.getId() + " saved to db");
        }

    }

    public synchronized boolean processPayment(Transaction t) {

        Account source = businessService.queryAccountById(t.getSourceAccount());
        Account dest = businessService.queryAccountById(t.getDestAccount());
        source.fundSubstract(t.getAmount());
        dest.fundAddition(t.getAmount());

        try {
            businessService.saveAccount(source);
            businessService.saveAccount(dest);
            logger.info(t.getAmount() + " were moved from account " + t.getSourceAccount() + " to " + t.getDestAccount());
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }
}
