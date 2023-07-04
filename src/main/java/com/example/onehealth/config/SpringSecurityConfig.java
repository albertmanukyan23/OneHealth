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
                .requestMatchers("/patient/register").permitAll()
                .requestMatchers("/patient/verify").permitAll()
                .requestMatchers("/doctor/verify").permitAll()
                .requestMatchers("/patient/confirmation-page").permitAll()
                .requestMatchers("/patient/change-password-page").permitAll()
                .requestMatchers("/patient/changePassword").permitAll()
                .requestMatchers("/patient/confirmation-email").permitAll()
                .requestMatchers("/doctor/confirmation-page").permitAll()
                .requestMatchers("/doctor/change-password-page").permitAll()
                .requestMatchers("/doctor/changePassword").permitAll()
                .requestMatchers("/doctor/confirmation-email").permitAll()
                .requestMatchers(HttpMethod.GET,"/medicalServices").permitAll()
                .requestMatchers("/medicalServices/create","/medicalServices/remove").hasAuthority("ADMIN")
                .requestMatchers(HttpMethod.GET,"/patient/singlePage","/patient/appointments").hasAnyAuthority("PATIENT")
                .requestMatchers("/patient/update","/patient/delete","/patient").hasAnyAuthority("ADMIN")
                .requestMatchers("/doctor/reject","/doctor/appointments").hasAuthority("DOCTOR")
                .requestMatchers("/doctor/singlePage").hasAuthority("DOCTOR")
                .requestMatchers("/doctor/**").hasAuthority("ADMIN")
                .requestMatchers("/admin").hasAuthority("ADMIN")
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
