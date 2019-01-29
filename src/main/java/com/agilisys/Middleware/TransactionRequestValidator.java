package com.agilisys.Middleware;

import com.agilisys.Models.Status;
import com.agilisys.Models.Transaction;
import com.agilisys.Services.PaymentsService;

import java.util.Date;
import java.util.UUID;

public class TransactionRequestValidator implements PaymentMiddleware {

    private PaymentMiddleware next;
    private static final PaymentsService paymentsService = PaymentsService.getInstance();

    public TransactionRequestValidator() {
    }

    @Override
    public void setNext(PaymentMiddleware chain) {
        this.next = chain;
    }

    @Override
    public synchronized void process(Transaction t) {
        /* atomic block */
        t.setId(UUID.randomUUID().toString());
        t.setSubmissionDate(new Date());

        if (inputDataTypesValidation(t)) {

            if (isValidRequest(t)) {
                t.setStatus(Status.ACCEPTED);

            } else {

                t.setEndDate(new Date());
                t.setStatus(Status.DECLINED);

            }
            logger.info("Transaction " + t.getId() + " changed status to " + t.getStatus().toString());
            paymentsService.insertTransaction(t);
            logger.info("Transaction " + t.getId() + " saved to db");
        }

        this.next.process(t);

    }

    private boolean isValidRequest(Transaction tr) {

        return (isValidAmount(tr) && validAccounts(tr));
    }

    private boolean isValidAmount(Transaction tr) {
        if ((tr.getAmount() > 0)) {
            return true;
        } else {
            tr.setMessage("Transaction amount must be positive");
            return false;
        }
    }

    private boolean validAccounts(Transaction tr) {

        if (paymentsService.existingAccount(tr.getSourceAccount()) &&
                paymentsService.existingAccount(tr.getDestAccount()) &&
                paymentsService.enoughFounds(tr.getSourceAccount(), tr.getAmount())) {
            return true;
        } else {
            tr.setMessage("Source account not valid or funds not enough");
            return false;
        }

    }

    private boolean inputDataTypesValidation(Transaction tr) {
        if (((Double) tr.getAmount() instanceof Double) &&
                ((Integer) tr.getSourceAccount() instanceof Integer) &&
                ((Integer) tr.getDestAccount() instanceof Integer)) {
            return true;

        } else {
            tr.setMessage("Input data type validation failed");
            return false;
        }
    }

}
