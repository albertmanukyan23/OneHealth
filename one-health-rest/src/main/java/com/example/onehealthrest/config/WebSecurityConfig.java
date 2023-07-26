package com.example.onehealthrest.config;
import com.example.onehealthrest.filter.JWTAuthenticationTokenFilter;
import com.example.onehealthrest.security.JwtAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class WebSecurityConfig {
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsService userDetailsService;
    private final JwtAuthenticationEntryPoint authenticationEntryPoint;
    private final JWTAuthenticationTokenFilter jwtAuthenticationTokenFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf().disable()
                .exceptionHandling().authenticationEntryPoint(authenticationEntryPoint)
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeHttpRequests()
                .requestMatchers("/patients/register").permitAll()
                .requestMatchers("/users/auth").permitAll()
                .requestMatchers("/users/getImage").permitAll()
                .requestMatchers("users/{id}/image").hasAnyAuthority("ADMIN","PATIENT","DOCTOR")
                .requestMatchers("/users/activate-deactivate").hasAuthority("ADMIN")
                .requestMatchers("/users/update-password").hasAnyAuthority("ADMIN","PATIENT","DOCTOR")
                .requestMatchers(HttpMethod.GET,"/medical-services").permitAll()
                .requestMatchers("/medical-services/create","/medical-services/remove","medical-services/update/{id}").hasAuthority("ADMIN")
                .requestMatchers(HttpMethod.GET,"/patients/single-page","/patients/appointments").hasAnyAuthority("PATIENT")
                .requestMatchers("/patients/update/{id}","/patients/remove","/patients", "/patients/{id}").hasAnyAuthority("ADMIN")
                .requestMatchers(HttpMethod.GET,"/doctor/search" ,"/doctor/details/{id}").hasAnyAuthority("PATIENT","ADMIN")
                .requestMatchers("/doctor/search/patients-for-doctor").hasAuthority("DOCTOR")
                .requestMatchers("/doctor/appointments").hasAuthority("DOCTOR")
                .requestMatchers("/patients/update","/patients/delete","/patients").hasAnyAuthority("ADMIN")
                .requestMatchers("/doctors/reject","/doctor/appointments").hasAuthority("DOCTOR")
                .requestMatchers("/doctor/singlePage").hasAuthority("DOCTOR")
                .requestMatchers("/doctors").hasAuthority("ADMIN")
              //  .requestMatchers("/doctors/**").hasAuthority("ADMIN")
                .requestMatchers("/comment/create" ,"/comment/delete").hasAnyAuthority("PATIENT","ADMIN")
                .requestMatchers("/admin").hasAuthority("ADMIN")
                .requestMatchers("/departments/**").hasAuthority("ADMIN")
                .anyRequest().authenticated();
        httpSecurity.addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);
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
