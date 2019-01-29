package com.agilisys.RestServices;

import com.agilisys.Middleware.PaymentMiddleware;
import com.agilisys.Middleware.TransactionProcessor;
import com.agilisys.Middleware.TransactionRequestValidator;
import com.agilisys.Models.Transaction;
import com.agilisys.Services.InMemoryRepository;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/api")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class TransactionRestService {

    private static final PaymentMiddleware chain = getPaymentMiddleware();
    private InMemoryRepository repository = InMemoryRepository.getInstance();

    /* Build Chain of Responsability for payment processing */
    private static PaymentMiddleware getPaymentMiddleware() {

        PaymentMiddleware validator = new TransactionRequestValidator();
        PaymentMiddleware processor = new TransactionProcessor();
        validator.setNext(processor);

        return validator;

    }

    @POST
    @Path("/transaction")
    public Transaction submitTransaction(Transaction t) {

        chain.process(t);
        return t;
    }

    @GET
    @Path("/transaction/{uuid}")
    public Transaction queryTransaction(@PathParam(value = "uuid") String uuid) {
        return repository.queryTransaction(uuid);
    }

}
