package com.agilisys.Middleware;

import com.agilisys.Models.Currency;
import com.agilisys.Models.Status;
import com.agilisys.Models.Transaction;
import com.agilisys.Services.AccountService;
import com.agilisys.Services.InMemoryRepository;
import org.apache.commons.lang3.EnumUtils;

import java.util.Date;
import java.util.UUID;

public class TransactionRequestValidator implements PaymentMiddleware {

    private PaymentMiddleware next;
    private InMemoryRepository repository = InMemoryRepository.getInstance();
    private static final AccountService accountService = AccountService.getInstance();

    public TransactionRequestValidator() {
    }

    @Override
    public void setNext(PaymentMiddleware chain) {
        this.next = chain;
    }

    @Override
    public synchronized void process(Transaction t) throws Exception {
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
            repository.insertTransaction(t);
        } else {

            throw new Exception("Bad input");
        }

        this.next.process(t);

    }

    private boolean isValidRequest(Transaction tr) throws Exception {

        return (isValidAmount(tr) && isValidCurrency(tr) && validAccounts(tr));
    }

    private boolean isValidCurrency(Transaction tr) {

        if (!(tr.getCurrency() == null)) {
            return (EnumUtils.isValidEnum(Currency.class, tr.getCurrency().name()));
        } else {
            tr.setMessage("Currency not valid");
            return false;
        }
    }

    private boolean isValidAmount(Transaction tr) {
        return ((tr.getAmount() > 0));
    }

    private boolean validAccounts(Transaction tr) throws Exception {

        if (accountService.existingAccount(tr.getSourceAccount()) &&
                accountService.existingAccount(tr.getDestAccount()) &&
                accountService.enoughFounds(tr.getSourceAccount(), tr.getAmount())) {
            return true;
        } else {
            tr.setMessage("Account not valid source account funds not enough");
            return false;
        }

    }

    private boolean inputDataTypesValidation(Transaction tr) {
        if ( ((Double) tr.getAmount() instanceof Double) &&
                        ((Integer) tr.getSourceAccount() instanceof Integer) &&
                        ((Integer) tr.getDestAccount() instanceof Integer) &&
                        (tr.getCurrency() instanceof Currency)) {
            return true;
        } else {
            tr.setMessage("Input data type validation failed");
            return false;
        }
    }

}
