package nl.agility.commons.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.header.writers.StaticHeadersWriter;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
public class SecurityAutoConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
            .antMatchers("/actuator/health").permitAll()
            .antMatchers("/actuator/info").permitAll()
            .antMatchers("/actuator/**").hasRole("OPS")
            .antMatchers("/api/**").hasAnyRole("USER", "ADMIN")
            .anyRequest().authenticated()
            .and()
            .httpBasic()
            .and()
            .headers()
            .frameOptions().sameOrigin()
            .addHeaderWriter(new StaticHeadersWriter("Server", "Unknown"))
            .and()
            .sessionManagement().sessionCreationPolicy(STATELESS)
            .and()
            .csrf().disable();
    }

    @Bean
    @Override
    public UserDetailsService userDetailsService() {
        UserDetails aUser = User.withUsername("user")
            .passwordEncoder(PasswordEncoderFactories.createDelegatingPasswordEncoder()::encode)
            .password("user").roles("USER").build();

        UserDetails anAdministrator = User.withUsername("admin")
            .passwordEncoder(PasswordEncoderFactories.createDelegatingPasswordEncoder()::encode)
            .password("admin").roles("ADMIN").build();

        UserDetails anOperator = User.withUsername("ops")
            .passwordEncoder(PasswordEncoderFactories.createDelegatingPasswordEncoder()::encode)
            .password("ops").roles("OPS").build();

        InMemoryUserDetailsManager userDetailsManager = new InMemoryUserDetailsManager();

        userDetailsManager.createUser(aUser);
        userDetailsManager.createUser(anAdministrator);
        userDetailsManager.createUser(anOperator);

        return userDetailsManager;
    }

}
