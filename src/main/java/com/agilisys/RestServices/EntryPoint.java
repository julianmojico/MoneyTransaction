package com.agilisys.RestServices;

import com.agilisys.Middleware.PaymentMiddleware;
import com.agilisys.Middleware.TransactionProcessor;
import com.agilisys.Middleware.TransactionRequestValidator;
import com.agilisys.Models.Transaction;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/api")
public class EntryPoint {

    @POST
    @Path("/transaction")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Transaction submitTransaction(Transaction t) throws Exception {


            getPaymentMiddleware().process(t);

//        String createdContent = create(content);
//        return Response.created(createdUri).entity(Entity.text(createdContent)).build();
        return t;
    }


    private static PaymentMiddleware getPaymentMiddleware(){

        /* Construct Chain of Responsability for payment processing */
        PaymentMiddleware validator = null;
        try {

        validator = new TransactionRequestValidator();
        PaymentMiddleware processor = new TransactionProcessor();
        validator.setNext(processor);

        } catch (Exception e) {e.printStackTrace();}
        return validator;

    }

}
