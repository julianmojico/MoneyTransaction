package com.agilisys.Middleware;

import com.agilisys.Models.Status;
import com.agilisys.Models.Transaction;

import java.util.Date;
import java.util.UUID;

public class TransactionValidator implements PaymentMiddleware {

    private PaymentMiddleware next;

    public TransactionValidator() {
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
            businessService.insertTransaction(t);
            logger.info("Transaction " + t.getId() + " saved to db");
        }

        this.next.process(t);

    }

    private boolean isValidRequest(Transaction tr) {
        int source = tr.getSourceAccount();
        int dest = tr.getDestAccount();
        boolean isValid = (isValidAmount(tr) && validAccounts(tr) && (source != dest));
        tr.setMessage("Invalid transaction request; verify source and destination accounts are both valid");
        return isValid;
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

        if (businessService.existingAccount(tr.getSourceAccount()) &&
                businessService.existingAccount(tr.getDestAccount()) &&
                this.enoughFounds(tr.getSourceAccount(), tr.getAmount())) {
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

    public boolean enoughFounds(int id, double amount) {
        if (businessService.existingAccount(id)) {
            return businessService.queryAccountById(id).getCurrentFunds() >= amount;
        } else return false;
    }

}
