package com.agilisys.Configuration;

import com.agilisys.RestServices.TransactionRestService;
import com.agilisys.Services.PaymentsService;
import org.glassfish.jersey.server.ResourceConfig;

public class ApplicationResourceConfig extends ResourceConfig {

    public ApplicationResourceConfig() {

        ResourceConfig config = new ResourceConfig();
        config.register(TransactionRestService.class);
        config.register(PaymentsService.class);
        packages("com.agilisys");

    }

}
