package com.example.onehealth.config;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
public class SpringSecurityConfig {

    private final PasswordEncoder passwordEncoder;

    private final UserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf().disable()
                .authorizeHttpRequests()
                .requestMatchers(HttpMethod.GET, "/").permitAll()
                .requestMatchers("/img/**").permitAll()
                .requestMatchers("/css/**").permitAll()
                .requestMatchers("/js/**").permitAll()
                .requestMatchers("/fonts/**").permitAll()
                .requestMatchers("/patients/register").permitAll()
                .requestMatchers("/user/**").permitAll()
                .requestMatchers("/user/activate-deactivate-page").hasAuthority("ADMIN")
                .requestMatchers(HttpMethod.GET,"/medicalServices").permitAll()
                .requestMatchers("/medicalServices/create","/medicalServices/remove").hasAuthority("ADMIN")
                .requestMatchers(HttpMethod.GET,"/patient/singlePage","/patient/appointments").hasAnyAuthority("PATIENT")
                .requestMatchers("/patients/update","/patients/delete","/patients").hasAnyAuthority("ADMIN")
                .requestMatchers("/doctor/reject","/doctor/appointments").hasAuthority("DOCTOR")
                .requestMatchers("/doctor/singlePage").hasAuthority("DOCTOR")
                .requestMatchers("/doctor/**").hasAuthority("ADMIN")
                .requestMatchers("/departments/**").hasAuthority("ADMIN")
                .requestMatchers("/admin/**").hasAuthority("ADMIN")
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/customLogin").permitAll()
                .defaultSuccessUrl("/customSuccessLogIn",true)
                .loginProcessingUrl("/login").permitAll()
                .failureUrl("/customLogin?error=true")
                .and()
                .logout()
                .logoutSuccessUrl("/")
                .permitAll();

        return httpSecurity.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        return daoAuthenticationProvider;

    }
}
