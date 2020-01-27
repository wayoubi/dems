package ca.concordia.ginacody.comp6231.server;


import ca.concordia.ginacody.comp6231.services.EventManagementService;
import ca.concordia.ginacody.comp6231.services.EventManagementServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;

import java.rmi.RemoteException;
import java.util.Optional;

@SpringBootApplication
public class ServerRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServerRunner.class);

    @Autowired
    private ApplicationArguments applicationArguments;

    @Bean
    EventManagementService bookingService() {
        return new EventManagementServiceImpl();
    }

    @Bean
    MyRmiExporter exporter(EventManagementService implementation) {
        Optional<String> optional = Optional.ofNullable(applicationArguments.getSourceArgs()[0]);
        Class<EventManagementService> serviceInterface = EventManagementService.class;
        MyRmiExporter exporter = new MyRmiExporter();
        exporter.setServiceInterface(serviceInterface);
        exporter.setService(implementation);
        exporter.setServiceName(serviceInterface.getSimpleName() + optional.get());
        //exporter.setServiceName(serviceInterface.getSimpleName());
        exporter.setRegistryHost("localhost");
        exporter.setRegistryPort(1099);
        return exporter;
    }

    public static void main(String[] args) {
        SpringApplicationBuilder builder = new SpringApplicationBuilder(ServerRunner.class);
        builder.headless(false);
        builder.run(args);
    }
}
