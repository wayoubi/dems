package ca.concordia.ginacody.comp6231.client;

import ca.concordia.ginacody.comp6231.client.shell.ShellHelper;
import ca.concordia.ginacody.comp6231.services.CabBookingService;
import org.jline.terminal.Terminal;
import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStyle;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.remoting.rmi.RmiProxyFactoryBean;
import org.springframework.shell.jline.PromptProvider;

@Configuration
public class ClientBeanConfig {

    /**
     * This method creates PromptProvider bean.
     *
     * @return PromptProvider
     */
    @Bean
    public PromptProvider myPromptProvider() {
        return () -> new AttributedString("EDMS Client# ",
                AttributedStyle.DEFAULT.foreground(AttributedStyle.YELLOW).background(AttributedStyle.RED));
    }


    /**
     * This method creates ShellHelper bean.
     *
     * @param terminal terminal
     * @return ShellHelper
     */
    @Bean
    public ShellHelper shellHelper(@Lazy Terminal terminal) {
        return new ShellHelper(terminal);
    }

    /**
     *
     * @return
     */
    @Bean
    RmiProxyFactoryBean service() {
        RmiProxyFactoryBean rmiProxyFactory = new RmiProxyFactoryBean();
        rmiProxyFactory.setServiceUrl("rmi://localhost:1099/CabBookingService");
        rmiProxyFactory.setServiceInterface(CabBookingService.class);
        return rmiProxyFactory;
    }
}
