package com.agilisys;

import com.agilisys.Configuration.ApplicationResourceConfig;
import com.agilisys.Models.Account;
import com.agilisys.Models.Error;
import com.agilisys.Models.Transaction;
import com.agilisys.Services.PaymentsService;
import org.eclipse.jetty.http.HttpHeader;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.junit.Assert.assertEquals;


public class RestIntegrationTest extends JerseyTest {

    private static final PaymentsService paymentsService = PaymentsService.getInstance();
    private Transaction tr = new Transaction();

    @Before
    public void initObjects() {

        /*Initialize sample data*/
        this.tr.setSourceAccount(1);
        this.tr.setDestAccount(2);
        paymentsService.loadSampleData();

    }

    @Test
    public void testInvalidMethod() {
        Response output = target("/api/transaction").request().get();
        Error bodyOutput = output.readEntity(Error.class);
        assertEquals("HTTP 405 Method Not Allowed", bodyOutput.getErrorMessage());
        assertEquals("Method Not Allowed", bodyOutput.getStatusDescription());
        assertEquals(MediaType.APPLICATION_JSON, output.getHeaders().get(HttpHeader.CONTENT_TYPE.toString()).get(0));
    }

    @Test
    public void testAccount() {
        Response output = target("/api/account/1").request().get();
        Account bodyOutput = output.readEntity(Account.class);
        assertEquals("Julian", bodyOutput.getOwner());
        assertEquals(1, bodyOutput.getId());
        assertEquals(MediaType.APPLICATION_JSON, output.getHeaders().get(HttpHeader.CONTENT_TYPE.toString()).get(0));
        assertEquals(output.getStatus(), 200);
    }

    @Test
    public void resourceNotFound() {
        Response output = target("/api/unexistant").request(MediaType.APPLICATION_JSON_TYPE).get();
        Error bodyOutput = output.readEntity(Error.class);
        assertEquals("HTTP 404 Not Found", bodyOutput.getErrorMessage());
        assertEquals("Not Found", bodyOutput.getStatusDescription());
        assertEquals(MediaType.APPLICATION_JSON, output.getHeaders().get(HttpHeader.CONTENT_TYPE.toString()).get(0));
        assertEquals(output.getStatus(), 404);

    }

    @Test
    public void testBadMethod() {
        Response output = target("/api/transaction").request(MediaType.APPLICATION_JSON_TYPE).get();
        Error bodyOutput = output.readEntity(Error.class);
        assertEquals("HTTP 405 Method Not Allowed", bodyOutput.getErrorMessage());
        assertEquals("Method Not Allowed", bodyOutput.getStatusDescription());
        assertEquals(MediaType.APPLICATION_JSON, output.getHeaders().get(HttpHeader.CONTENT_TYPE.toString()).get(0));
        assertEquals(output.getStatus(), 405);

    }

    @Test
    public void testTransactionCompleted() {

        tr.setAmount(300);
        Response output = target("/api/transaction").request().post(Entity.entity(tr, MediaType.APPLICATION_JSON));
        Transaction bodyOutput = output.readEntity(Transaction.class);
        assertEquals(bodyOutput.getMessage(), "Transaction succesfully completed");
        assertEquals(MediaType.APPLICATION_JSON, output.getHeaders().get(HttpHeader.CONTENT_TYPE.toString()).get(0));
        assertEquals(bodyOutput.getStatus().toString(), "COMPLETED");
        assertEquals(output.getStatus(), 200);

    }

    @Test
    public void testTransactionDeclined() {

        tr.setAmount(99999);
        Response output = target("/api/transaction").request().post(Entity.entity(tr, MediaType.APPLICATION_JSON_TYPE));
        Transaction bodyOutput = output.readEntity(Transaction.class);
        assertEquals(bodyOutput.getMessage(), "Source account not valid or funds not enough");
        assertEquals(MediaType.APPLICATION_JSON, output.getHeaders().get(HttpHeader.CONTENT_TYPE.toString()).get(0));
        assertEquals(bodyOutput.getStatus().toString(), "DECLINED");
        assertEquals(output.getStatus(), 200);

    }

    @Test
    public void queryTransaction() {

        Response output = target("/api/transaction/1234").request().get();
        Transaction bodyOutput = output.readEntity(Transaction.class);
        assertEquals(bodyOutput.getId(), "1234");
        assertEquals(bodyOutput.getAmount(), 100.0D, 0);
        assertEquals(bodyOutput.getStatus().toString(), "COMPLETED");
        assertEquals(MediaType.APPLICATION_JSON, output.getHeaders().get(HttpHeader.CONTENT_TYPE.toString()).get(0));
        assertEquals(output.getStatus(), 200);

    }

    /* Implementation required by JerseyTest*/
    public Application configure() {

        ApplicationResourceConfig config = new ApplicationResourceConfig();
        return config.getApplication();
    }

}
