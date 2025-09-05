package org.apache.struts.examples.mailreader2.config;

import java.util.ArrayList;
import java.util.Collection;
import org.apache.struts.examples.mailreader2.dao.UserDatabase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public InMemoryUserDetailsManager inMemoryUserDetailsManager(UserDatabase userDatabase) {
        Collection<UserDetails> userDetails = new ArrayList<>();

        for (org.apache.struts.examples.mailreader2.dao.User user : userDatabase
                .findUsers()) {
            UserDetails ud =
                    User.withUsername(user.getUsername())
                            .password("{noop}" + user.getPassword())
                            .authorities("ROLE_user").build();
            userDetails.add(ud);
        }

        return new InMemoryUserDetailsManager(userDetails);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/tour.html", "/css/**", "/",
                                "/index.html", "/welcome",
                                "/login", "/login/cancel",
                                "/struts-power.gif", "/favicon.ico", "/registration")
                        .permitAll()
                        .anyRequest().authenticated())
                .logout((logout) -> logout.logoutSuccessUrl("/welcome")
                        .permitAll())
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/login")));

        return http.build();
    }

    // Required for manual authentication
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

}
