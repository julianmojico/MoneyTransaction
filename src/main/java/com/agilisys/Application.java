package com.agilisys;

import com.agilisys.Configuration.ApplicationResourceConfig;
import com.agilisys.Services.InMemoryRepository;
import com.agilisys.Services.PaymentsService;
import org.eclipse.jetty.server.Server;
import org.glassfish.jersey.jetty.JettyHttpContainerFactory;

import javax.ws.rs.core.UriBuilder;
import java.net.URI;

public class Application {

    private static final PaymentsService accountService = PaymentsService.getInstance();

    public static void main(String[] args) {

        try {
            /*Initialize Jetty server */
            URI baseUri = UriBuilder.fromUri("http://localhost/").port(8000).build();
            ApplicationResourceConfig config = new ApplicationResourceConfig();
            Server server = JettyHttpContainerFactory.createServer(baseUri, config);
            server.start();

            /* Initialize repository*/
            InMemoryRepository.getInstance();

            /*Initialize sample data*/
            accountService.loadSampleData();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    }

