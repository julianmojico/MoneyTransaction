package com.agilisys.RestServices;

import com.agilisys.Models.Account;
import com.agilisys.Models.Transaction;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Api("Account")
@Path("/api/account/{id}")
@Produces(MediaType.APPLICATION_JSON)
public class AccountRestService {

    private BusinessService repository = BusinessService.getInstance();

    @GET
    @ApiOperation(value = "Find account by id",
            notes = "Only one account can be retrieved per request",
            response = Account.class,
            responseContainer = "Json object",
            produces = MediaType.APPLICATION_JSON,
            code = 200)
    @ApiParam(value = "accountId", example = "1")
    public Account queryAccount(@PathParam(value = "id") int id) {
        return repository.queryAccountById(id);
    }

    @GET
    @Path("/transactions")
    @ApiOperation(value = "Retrieve transactions made from or to this account",
            response = Transaction.class,
            responseContainer = "Json object",
            produces = MediaType.APPLICATION_JSON,
            code = 200)
    @ApiParam(value = "accountId", example = "1")
    public List<Transaction> retrieveTransactions(@PathParam(value = "id") int id) {
        return repository.retrieveAccountTransactions(id);
    }


    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Account createAccount(Account acc) {
        int newId = repository.insertAccount(acc);
        return repository.queryAccountById(newId);
    }

    @DELETE
    public void deleteAccount(int id) {
        repository.deleteAccount(id);
    }

    @PATCH
    @Consumes(MediaType.APPLICATION_JSON)
    public Account patchAccount(Account account) {

        repository.patchAccount(account);
        return repository.queryAccountById(account.getId());
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public Account putAccount(Account account) {

        repository.patchAccount(account);
        return repository.queryAccountById(account.getId());

    }

}
