package com.agilisys.RestServices;

import com.agilisys.Models.Account;
import com.agilisys.Services.BusinessService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Api("Account")
@Path("/api")
@Produces(MediaType.APPLICATION_JSON)
public class AccountRestService {

    private BusinessService repository = BusinessService.getInstance();

    @GET
    @Path("/account/{id}")
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

    @POST
    @Path("/account")
    @Consumes(MediaType.APPLICATION_JSON)
    public Account createAccount(Account acc) {
        int newId = repository.insertAccount(acc);
        return repository.queryAccountById(newId);
    }

    @DELETE
    @Path("/account/{id}")
    public void deleteAccount(int id) {
        repository.deleteAccount(id);
    }

    @PATCH
    @Path("/account")
    @Consumes(MediaType.APPLICATION_JSON)
    public Account patchAccount(Account account) {

        repository.patchAccount(account);
        return repository.queryAccountById(account.getId());
    }

    @PUT
    @Path("/account")
    @Consumes(MediaType.APPLICATION_JSON)
    public Account putAccount(Account account) {

        repository.patchAccount(account);
        return repository.queryAccountById(account.getId());

    }

}
