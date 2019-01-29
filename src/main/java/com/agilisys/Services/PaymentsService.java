package com.agilisys.Services;

import com.agilisys.Models.Account;
import com.agilisys.Models.Status;
import com.agilisys.Models.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicInteger;

public class PaymentsService {

    private static final Logger logger = LoggerFactory.getLogger(PaymentsService.class);
    private static final PaymentsService paymentsService = new PaymentsService();
    private InMemoryRepository repository = InMemoryRepository.getInstance();

    private PaymentsService() {
    }

    public static PaymentsService getInstance() {

        return paymentsService;
    }

    public boolean existingAccount(int id) {

        Account foundAccount = repository.queryAccount(id);
        return foundAccount != null;
    }

    public Account queryAccount(int id) {
        return repository.queryAccount(id);
    }


    public boolean enoughFounds(int id, double amount) {
        if (existingAccount(id)) {
            return queryAccount(id).getCurrentFunds() >= amount;
        } else return false;
    }

    public synchronized boolean processPayment(Transaction t) {

        Account source = repository.queryAccount(t.getSourceAccount());
        Account dest = repository.queryAccount(t.getDestAccount());
        source.fundSubstract(t.getAmount());
        dest.fundAddition(t.getAmount());

        try {
            repository.saveAccount(source);
            repository.saveAccount(dest);
            logger.info(t.getAmount() + " were moved from account " + t.getSourceAccount() + " to " + t.getDestAccount());
            return true;

        } catch (Exception e) {
            //TODO: MEJORAR EXCEPTION Y MENSAJE DE ERROR. QUE HACEMOS CON LA TRANSACCION?
            e.printStackTrace();
            return false;
        }

    }

    public void loadSampleData() {

        AtomicInteger idGenerator = new AtomicInteger();

        Account user1 = new Account(idGenerator.incrementAndGet(), "Julian");
        Account user2 = new Account(idGenerator.incrementAndGet(), "Peter");

        user1.fundAddition(2000);
        user2.fundAddition(1000);

        repository.deleteAccounts();
        repository.insertAccount(user1);
        repository.insertAccount(user2);

        Transaction sampleTransaction = new Transaction();
        sampleTransaction.setAmount(100);
        sampleTransaction.setId("1234");
        sampleTransaction.setDestAccount(2);
        sampleTransaction.setSourceAccount(1);
        sampleTransaction.setStatus(Status.COMPLETED);

        repository.deleteTransactionById("1234");
        repository.insertTransaction(sampleTransaction);


    }

    public void removeTransaction(String uuid) {
        repository.deleteTransactionById(uuid);
    }

    public void insertTransaction(Transaction t) {
        repository.insertTransaction(t);
    }

    public void saveTransaction(Transaction t) {
        repository.saveTransaction(t);
    }
}