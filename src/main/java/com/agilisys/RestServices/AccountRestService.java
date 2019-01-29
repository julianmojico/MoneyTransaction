package com.agilisys.RestServices;

import com.agilisys.Models.Account;
import com.agilisys.Services.PaymentsService;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;


@Path("/api")
@Produces(MediaType.APPLICATION_JSON)
public class AccountRestService {

    private static final PaymentsService paymentsService = PaymentsService.getInstance();

    @GET
    @Path("/account/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Account queryAccount(@PathParam(value = "id") int id) {
        return paymentsService.queryAccount(id);
    }

}
