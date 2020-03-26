package nl.agility.commons.web.config;

import nl.agility.commons.web.filters.CustomMdcFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterAutoConfiguration {

    @Bean
    public FilterRegistrationBean customMdcFilter() {
        FilterRegistrationBean<CustomMdcFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new CustomMdcFilter());
        registrationBean.addUrlPatterns("/api/*");

        return registrationBean;
    }

}
