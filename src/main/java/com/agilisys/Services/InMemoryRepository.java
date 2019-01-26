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

    public void insertTransaction(Transaction t){
        db.insert(t);
    }

    public void saveTransaction(Transaction t){
        db.save(t,Transaction.class);
    }

    public void saveAccount(Account acc){
        db.save(acc, Account.class);
    }

    public Object queryTransaction(String id){
       return db.findById(id, Transaction.class);
    }

    public Account queryAccount(int id){
        return db.findById(id, Account.class);
    }

    public void insertAccount(Account account) {
        db.insert(account);
    }
}
