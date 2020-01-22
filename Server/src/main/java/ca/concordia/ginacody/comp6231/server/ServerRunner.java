package ca.concordia.ginacody.comp6231.server;

import ca.concordia.ginacody.comp6231.services.CabBookingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.remoting.rmi.RmiServiceExporter;

import java.util.Optional;

@SpringBootApplication
public class ServerRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServerRunner.class);

    @Autowired
    private ApplicationArguments applicationArguments;

    @Bean
    CabBookingService bookingService() {
        return new CabBookingServiceImpl();
    }

    @Bean
    MyRmiExporter exporter(CabBookingService implementation) {
        Optional<String> optional = Optional.ofNullable(applicationArguments.getSourceArgs()[0]);
        Class<CabBookingService> serviceInterface = CabBookingService.class;
        MyRmiExporter exporter = new MyRmiExporter();
        exporter.setServiceInterface(serviceInterface);
        exporter.setService(implementation);
        //exporter.setServiceName(serviceInterface.getSimpleName() + optional.get());
        exporter.setServiceName(serviceInterface.getSimpleName());
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
