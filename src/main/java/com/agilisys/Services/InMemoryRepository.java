package com.agilisys.Services;

import com.agilisys.Models.Account;
import com.agilisys.Models.Transaction;
import io.jsondb.JsonDBTemplate;

public class InMemoryRepository {
    private static InMemoryRepository instance;
    private static JsonDBTemplate db;

    public static InMemoryRepository getInstance() {

        if (instance == null) {
            instance = new InMemoryRepository();
        }
        return instance;
    }

    private InMemoryRepository() {

        String dbFilesLocation = "./datastore";
        String baseScanPackage = "com.agilisys.Models";
        JsonDBTemplate jsonDBTemplate = new JsonDBTemplate(dbFilesLocation, baseScanPackage);

        try {
            if (!jsonDBTemplate.collectionExists(Account.class)) {
                jsonDBTemplate.createCollection(Account.class);
            }

            if (!jsonDBTemplate.collectionExists(Transaction.class)) {
                jsonDBTemplate.createCollection(Transaction.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        db = jsonDBTemplate;
    }

    void insertTransaction(Transaction t) {
        db.insert(t);
    }

    void saveTransaction(Transaction t) {
        db.save(t, Transaction.class);
    }

    void saveAccount(Account acc) {
        db.save(acc, Account.class);
    }

    public Transaction queryTransaction(String id) {
        return db.findById(id, Transaction.class);
    }

    Account queryAccount(int id) {
        return db.findById(id, Account.class);
    }

    void insertAccount(Account account) {
        db.insert(account);
    }

    void deleteAccounts() {
        db.dropCollection(Account.class);
        db.createCollection(Account.class);
    }

    void deleteTransactionById(String uuid) {
        String jxQuery = String.format("/.[id='%s']", "1234");
        db.findAndRemove(jxQuery, Transaction.class);
//        db.findOne(jxQuery, Transaction.class);
    }

}
