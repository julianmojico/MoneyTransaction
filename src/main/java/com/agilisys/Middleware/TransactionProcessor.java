package com.agilisys.Middleware;

import com.agilisys.Models.Status;
import com.agilisys.Models.Transaction;
import com.agilisys.Services.AccountService;
import com.agilisys.Services.InMemoryRepository;

import java.util.Date;

public class TransactionProcessor implements PaymentMiddleware {

    private PaymentMiddleware next;
    private InMemoryRepository repository = InMemoryRepository.getInstance();
    private static final AccountService accountService = AccountService.getInstance();

    @Override
    public void setNext(PaymentMiddleware next) {
        this.next = next;
    }

    @Override
    public synchronized void process(Transaction t) {
        /* atomic block */

    if (t.getStatus() == Status.ACCEPTED){


            accountService.processPayment(t);

        if ( validTransaction(t)) {
            t.setEndDate(new Date());
            t.setStatus(Status.COMPLETED);
        } else {
            t.setStatus(Status.SUSPENDED);
            //TODO: Y aca que pinta?
        }
        repository.saveTransaction(t);
    }
    //TODO: Y aca que pinta?
    }

    private boolean validTransaction(Transaction t) {
        //TODO: Como validar transaccion?
        return true;
    }

}
