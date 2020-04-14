package com.agilisys.RestServices;

import com.agilisys.Models.Account;
import com.agilisys.Models.Status;
import com.agilisys.Models.Transaction;
import io.jsondb.JsonDBTemplate;

import javax.ws.rs.BadRequestException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

public class BusinessService {

    private static final AtomicInteger idGenerator = new AtomicInteger();
    private static final Logger logger = Logger.getLogger("MoneyTransactionAPI");
    private static BusinessService instance;
    private static JsonDBTemplate repository;

    private BusinessService() {

        String dbFilesLocation = "./datastore";
        String baseScanPackage = "com.agilisys.Models";
        JsonDBTemplate jsonDBTemplate = new JsonDBTemplate(dbFilesLocation, baseScanPackage);
        repository = jsonDBTemplate;

        try {
            if (!jsonDBTemplate.collectionExists(Account.class)) {
                repository.createCollection(Account.class);
            }

            if (!jsonDBTemplate.collectionExists(Transaction.class)) {
                repository.createCollection(Transaction.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static BusinessService getInstance() {

        if (instance == null) {
            instance = new BusinessService();
        }
        return instance;
    }

    public void insertTransaction(Transaction t) {
        repository.insert(t);
    }

    public void saveTransaction(Transaction t) {
        repository.save(t, Transaction.class);
    }

    public void patchAccount(Account newFields) {

        Account foundAccount = queryAccountById(newFields.getId());
        if (foundAccount != null) {

            foundAccount.updateAccount(newFields);
            repository.save(foundAccount, Account.class);

        } else {
            throw new BadRequestException("Account Id not found");
        }
    }

    public void saveAccount(Account acc) {
        repository.save(acc, Account.class);
    }

    public Transaction queryTransaction(String id) {
        return repository.findById(id, Transaction.class);
    }

    public Account queryAccountById(int id) {
        return repository.findById(id, Account.class);
    }

    public List<Transaction> retrieveAccountTransactions(int id) {
        ArrayList output = new ArrayList<>();
        String jxQuerySource = String.format("/.[sourceAccount='%s']", id);
        String jxQueryDest = String.format("/.[destAccount='%s']", id);
        Collection sourceTransactions = repository.find(jxQuerySource, Transaction.class);
        Collection destTransactions = repository.find(jxQueryDest, Transaction.class);
        if (!sourceTransactions.isEmpty()) {
            output.addAll(sourceTransactions);
        }
        if (!destTransactions.isEmpty()) {
            output.addAll(destTransactions);
        }
        return output;
    }

    public void deleteAccounts() {
        repository.dropCollection(Account.class);
        repository.createCollection(Account.class);
    }

    public void deleteTransactionById(String uuid) {
        String jxQuery = String.format("/.[id='%s']", uuid);
        repository.findAndRemove(jxQuery, Transaction.class);
    }


    public int insertAccount(Account acc) throws BadRequestException {

        if (acc.getOwner() != null) {
            acc.setId(idGenerator.incrementAndGet());
            acc.setCreationDate(new Date());
            repository.insert(acc);
            return acc.getId();
        } else {
            throw new BadRequestException("Account owner cannot be null");
        }
    }

    public boolean existingAccount(int id) {

        Account foundAccount = queryAccountById(id);
        return foundAccount != null;
    }

    public void deleteAccount(int id) {
        String jxQuery = String.format("/.[id='%s']", id);
        repository.findAndRemove(jxQuery, Account.class);
    }

    public void loadSampleData() throws BadRequestException {


        Account user1 = new Account("Julian");
        Account user2 = new Account("Peter");

        user1.fundAddition(2000);
        user2.fundAddition(1000);

        deleteAccounts();
        insertAccount(user1);
        insertAccount(user2);

        Transaction sampleTransaction = new Transaction();
        sampleTransaction.setAmount(100);
//        sampleTransaction.setId("1234");
        sampleTransaction.setDestAccount(2);
        sampleTransaction.setSourceAccount(1);
        sampleTransaction.setStatus(Status.COMPLETED);
        sampleTransaction.setEndDate(new Date());

//        deleteTransactionById("1234");
        insertTransaction(sampleTransaction);


    }

}
