package com.agilisys;

import com.agilisys.Exceptions.GenericExceptionMapper;
import com.agilisys.RestServices.EntryPoint;
import com.agilisys.Services.AccountService;
import com.agilisys.Services.InMemoryRepository;
import org.eclipse.jetty.server.Server;
import org.glassfish.jersey.jetty.JettyHttpContainerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import javax.ws.rs.core.UriBuilder;
import java.net.URI;

public class App {

    private static final AccountService accountService = AccountService.getInstance();
    public static void main(String[] args) throws Exception {

            /*Initialize Jetty server */
            URI baseUri = UriBuilder.fromUri("http://localhost/").port(8080).build();
            ResourceConfig config = new ResourceConfig();
            config.register(EntryPoint.class);
            config.register(GenericExceptionMapper.class);
            Server server = JettyHttpContainerFactory.createServer(baseUri, config);
            server.start();

            /* Initialize repository*/
            InMemoryRepository.getInstance();

            /*Initialize sample data*/
            accountService.loadSampleData();
        }

    }

