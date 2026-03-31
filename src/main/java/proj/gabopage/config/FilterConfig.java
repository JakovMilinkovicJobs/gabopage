package proj.gabopage.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<IpWhitelistFilter> ipWhitelistFilterRegistration(IpWhitelistFilter filter) {
        FilterRegistrationBean<IpWhitelistFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(filter);
        registration.addUrlPatterns("/login", "/login/*", "/admin/*");
        registration.setName("ipWhitelistFilter");
        registration.setOrder(1);
        return registration;
    }
}
