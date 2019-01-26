package com.agilisys.Services;

import com.agilisys.Models.Account;
import com.agilisys.Models.Transaction;

import javax.security.auth.login.AccountNotFoundException;
import java.util.concurrent.atomic.AtomicInteger;

public class AccountService {
    private static AccountService ourInstance = new AccountService();
    private InMemoryRepository repository = InMemoryRepository.getInstance();

    public static AccountService getInstance() {

        return ourInstance;
    }

    private AccountService() {
    }

    public boolean existingAccount(int id) {

        Account foundAccount = repository.queryAccount(id);
        return foundAccount != null;
    }

    public Account queryAccount(int id){
        return repository.queryAccount(id);
    }


    public boolean enoughFounds(int id, double amount) throws AccountNotFoundException {
        if (existingAccount(id)){
            return queryAccount(id).getCurrentFunds() >= amount;
        } else throw new AccountNotFoundException("Account" + id + " does not exist");
    }

    public boolean processPayment(Transaction t) {

        Account source = repository.queryAccount(t.getSourceAccount());
        Account dest = repository.queryAccount(t.getDestAccount());
        source.fundSubstract(t.getAmount());
        dest.fundAddition(t.getAmount());

        try {
            repository.saveAccount(source);
            repository.saveAccount(dest);
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

        if (!existingAccount(user1.getId())) {
            repository.insertAccount(user1);
        }

        if (!existingAccount(user2.getId())) {
            repository.insertAccount(user2);
        }
    }

}