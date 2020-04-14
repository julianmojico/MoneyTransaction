package com.agilisys.Configuration;

import com.agilisys.RestServices.TransactionRestService;
import io.swagger.jaxrs.config.BeanConfig;
import io.swagger.jaxrs.listing.ApiListingResource;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.resource.Resource;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;

import java.net.URISyntaxException;

public class ApplicationResourceConfig extends ResourceConfig {

    // Holds handlers
    final HandlerList handlers = new HandlerList();

    public ApplicationResourceConfig() {


        Resource.setDefaultUseCaches(false);
        // Build the Swagger Bean.
        buildSwagger();
        // Handler for Swagger UI, static handler.
        handlers.addHandler(buildSwaggerUI());
        // Handler for Entity Browser and Swagger
        handlers.addHandler(buildContext());
    }

    private static void buildSwagger() {
        // This configures Swagger
        BeanConfig beanConfig = new BeanConfig();
        beanConfig.setVersion("1.0.0");
        beanConfig.setResourcePackage(TransactionRestService.class.getPackage().getName());
        beanConfig.setScan(true);
        beanConfig.setBasePath("/");
        beanConfig.setDescription("MoneyTransaction API, Swagger Jersey2 integrated and "
                + "embedded Jetty instance, with no web.xml or Spring MVC.");
        beanConfig.setTitle("MoneyTransactionAPI");
    }

    private static ContextHandler buildContext() {
        ResourceConfig resourceConfig = new ResourceConfig();
        resourceConfig.register(new CORSFilter());
        resourceConfig.packages(TransactionRestService.class.getPackage().getName(), ApiListingResource.class.getPackage().getName());
        ServletContainer servletContainer = new ServletContainer(resourceConfig);
        ServletHolder entityBrowser = new ServletHolder(servletContainer);
        ServletContextHandler entityBrowserContext = new ServletContextHandler(ServletContextHandler.NO_SESSIONS);
        entityBrowserContext.setContextPath("/");
        entityBrowserContext.addServlet(entityBrowser, "/*");

        return entityBrowserContext;
    }

    private static ContextHandler buildSwaggerUI() {

        final ResourceHandler swaggerUIResourceHandler = new ResourceHandler();
        try {
            swaggerUIResourceHandler.setResourceBase(ApplicationResourceConfig.class.getClassLoader().getResource("swaggerui").toURI().toString());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        final ContextHandler swaggerUIContext = new ContextHandler();
        swaggerUIContext.setContextPath("/docs/");
        swaggerUIContext.setHandler(swaggerUIResourceHandler);

        return swaggerUIContext;
    }

    public HandlerList getHandlers() {
        return this.handlers;
    }


}
