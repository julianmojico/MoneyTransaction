package com.agilisys.RestServices;

import com.agilisys.Middleware.PaymentMiddleware;
import com.agilisys.Middleware.TransactionProcessor;
import com.agilisys.Middleware.TransactionValidator;
import com.agilisys.Models.Transaction;
import com.agilisys.Services.BusinessService;
import io.swagger.annotations.*;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Api("Transaction")
@Path("/api")
@ApiModel(subTypes = {Transaction.class})
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)

public class TransactionRestService {

    private static final PaymentMiddleware chain = getPaymentMiddleware();
    private BusinessService repository = BusinessService.getInstance();

    /* Method that builds and return Chain of Responsability for payment processing */
    private static PaymentMiddleware getPaymentMiddleware() {


        PaymentMiddleware validator = new TransactionValidator();
        PaymentMiddleware processor = new TransactionProcessor();
        validator.setNext(processor);

        return validator;

    }

    @POST
    @Path("/transaction")
    @ApiOperation(value = "Submit new Transaction",
            notes = "A transaction request can be initiated using sourceAccount,destAccount and funds. The rest of the fields are fullfilled by the payment process",
            response = Transaction.class,
            responseReference = "Transaction result")
    public Transaction submitTransaction(Transaction t) {

        chain.process(t);
        return t;
    }
    /*Transactions are not meant to be deleted or modified hence no DELETE or PATCH methods exist*/

    @GET
    @ApiModelProperty(required = true, example = "1234")
    @Path("/transaction/{uuid}")
    public Transaction queryTransaction(@PathParam(value = "uuid") @ApiParam(value = "uuid") String uuid) {
        return repository.queryTransaction(uuid);
    }


}
