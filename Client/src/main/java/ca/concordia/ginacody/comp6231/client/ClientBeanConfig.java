package ca.concordia.ginacody.comp6231.client;

import ca.concordia.ginacody.comp6231.client.cache.SessionUser;
import ca.concordia.ginacody.comp6231.client.rmi.CabBookingServiceFactoryBean;
import ca.concordia.ginacody.comp6231.client.shell.ShellHelper;
import org.jline.terminal.Terminal;
import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStyle;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.shell.jline.PromptProvider;

@Configuration
public class ClientBeanConfig {

    @Bean
    public PromptProvider myPromptProvider() {
        return () -> new AttributedString("EDMS Client# ",
                AttributedStyle.DEFAULT.foreground(AttributedStyle.YELLOW).background(AttributedStyle.RED));
    }

    @Bean
    public ShellHelper shellHelper(@Lazy Terminal terminal) {
        return new ShellHelper(terminal);
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    CabBookingServiceFactoryBean service(SessionUser sessionUser) {
        return new CabBookingServiceFactoryBean(sessionUser);
    }
}
