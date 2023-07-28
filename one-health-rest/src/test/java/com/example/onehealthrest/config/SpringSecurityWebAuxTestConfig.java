package com.example.onehealthrest.config;

import com.example.onehealthcommon.entity.Patient;
import com.example.onehealthcommon.entity.User;
import com.example.onehealthcommon.entity.UserType;
import com.example.onehealthrest.security.CurrentUser;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import java.util.Arrays;
import java.util.Date;

@TestConfiguration
public class SpringSecurityWebAuxTestConfig {
    @Bean
    @Primary
    public UserDetailsService userDetailsService() {
        User basicUser1 = User.builder()
                .id(1)
                .email("albertmanukyan@mail.com")
                .name("poxos")
                .surname("poxosyan")
                .password("poxosi")
                .token(null)
                .birthDate(new Date())
                .enabled(true)
                .userType(UserType.ADMIN).build();

        CurrentUser currentUser1 = new CurrentUser(basicUser1);

        return new InMemoryUserDetailsManager(Arrays.asList(
                currentUser1
        ));
    }
}
