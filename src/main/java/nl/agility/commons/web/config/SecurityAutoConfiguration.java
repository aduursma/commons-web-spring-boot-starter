package nl.agility.commons.web.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.header.writers.StaticHeadersWriter;

@Configuration(proxyBeanMethods = false)
public class SecurityAutoConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
            .antMatchers("/actuator/health").permitAll()
            .antMatchers("/actuator/info").permitAll()
            .antMatchers("/actuator/**").hasRole("OPERATIONS")
            .anyRequest().permitAll()
            .and()
            .httpBasic()
            .and()
            .headers()
            .frameOptions().sameOrigin()
            .addHeaderWriter(new StaticHeadersWriter("Server", "Unknown"))
            .and()
            .csrf().disable();
    }

}
