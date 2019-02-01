package com.agilisys;

import com.agilisys.Models.Account;
import com.agilisys.Models.Error;
import com.agilisys.Models.Transaction;
import com.agilisys.RestServices.AccountRestService;
import com.agilisys.RestServices.TransactionRestService;
import com.agilisys.Services.BusinessService;
import org.eclipse.jetty.http.HttpHeader;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.jetty.JettyTestContainerFactory;
import org.glassfish.jersey.test.spi.TestContainerFactory;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.junit.Assert.assertEquals;


public class RestIntegrationTest extends JerseyTest {

    private static final BusinessService repository = BusinessService.getInstance();
    private static Transaction tr = new Transaction();

    @BeforeClass
    public static void initObjects() {

        /*Initialize sample data*/
        tr.setSourceAccount(1);
        tr.setDestAccount(2);
        repository.loadSampleData();

    }

    @Override
    public TestContainerFactory getTestContainerFactory() {
        return new JettyTestContainerFactory();
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
    public void getAccount() {
        Response output = target("/api/account/1").request().get();
        Account bodyOutput = output.readEntity(Account.class);
        assertEquals("Julian", bodyOutput.getOwner());
        assertEquals(1, bodyOutput.getId());
        assertEquals(MediaType.APPLICATION_JSON, output.getHeaders().get(HttpHeader.CONTENT_TYPE.toString()).get(0));
        assertEquals(output.getStatus(), 200);
    }

    @Test
    public void testResourceNotFound() {
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
    public void postTransactionCompleted() {

        tr.setAmount(300);
        Response output = target("/api/transaction").request().post(Entity.entity(tr, MediaType.APPLICATION_JSON));
        Transaction bodyOutput = output.readEntity(Transaction.class);
        assertEquals("Transaction succesfully completed", bodyOutput.getMessage());
        assertEquals(MediaType.APPLICATION_JSON, output.getHeaders().get(HttpHeader.CONTENT_TYPE.toString()).get(0));
        assertEquals("COMPLETED", bodyOutput.getStatus().toString());
        assertEquals(200, output.getStatus());

    }

    @Test
    public void postTransactionDeclined() {

        tr.setAmount(99999);
        Response output = target("/api/transaction").request().post(Entity.entity(tr, MediaType.APPLICATION_JSON_TYPE));
        Transaction bodyOutput = output.readEntity(Transaction.class);
        assertEquals(bodyOutput.getMessage(), "Source account not valid or funds not enough");
        assertEquals(MediaType.APPLICATION_JSON, output.getHeaders().get(HttpHeader.CONTENT_TYPE.toString()).get(0));
        assertEquals(bodyOutput.getStatus().toString(), "DECLINED");
        assertEquals(output.getStatus(), 200);

    }

    @Test
    public void getTransaction() {

        Response output = target("/api/transaction/1234").request().get();
        Transaction bodyOutput = output.readEntity(Transaction.class);
        assertEquals(bodyOutput.getId(), "1234");
        assertEquals(bodyOutput.getAmount(), 100.0D, 0);
        assertEquals(bodyOutput.getStatus().toString(), "COMPLETED");
        assertEquals(MediaType.APPLICATION_JSON, output.getHeaders().get(HttpHeader.CONTENT_TYPE.toString()).get(0));
        assertEquals(output.getStatus(), 200);

    }

    @Test
    public void putAccount() {

        Account account = new Account();
        account.setId(2);
        account.setOwner("NewOwner");
        Response output = target("/api/account").request().put(Entity.entity(account, MediaType.APPLICATION_JSON));

        Account bodyOutput = output.readEntity(Account.class);
        assertEquals(account.getId(), bodyOutput.getId());
        assertEquals(account.getCurrentFunds(), bodyOutput.getCurrentFunds(), 0);
        assertEquals(account.getOwner(), "NewOwner");
        assertEquals(MediaType.APPLICATION_JSON, output.getHeaders().get(HttpHeader.CONTENT_TYPE.toString()).get(0));
        assertEquals(output.getStatus(), 200);

    }

    /* Minimal Application implementation for JerseyTest*/
    public Application configure() {

        ResourceConfig config = new ResourceConfig();
        config.register(TransactionRestService.class);
        config.register(AccountRestService.class);
        config.setApplicationName("MoneyTransactionAPI");
        config.getConfiguration();
        config.packages("com.agilisys");
        return config.getApplication();
    }

}
