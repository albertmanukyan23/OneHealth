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
                .requestMatchers("/swagger-ui/**").permitAll()
                .requestMatchers("/v3/api-docs/**").permitAll()
                .requestMatchers("/users/auth","/users/getImage","/users/verify-account").permitAll()
                .requestMatchers("/cart/**").hasAuthority("ADMIN")
                .requestMatchers("/patients/register").permitAll()
                .requestMatchers("users/{id}/image","users/single-page").hasAnyAuthority("ADMIN","PATIENT","DOCTOR")
                .requestMatchers("/users/activate-deactivate").hasAuthority("ADMIN")
                .requestMatchers("/users/update-password").hasAnyAuthority("ADMIN","PATIENT","DOCTOR")
                .requestMatchers(HttpMethod.GET,"/medical-services").permitAll()
                .requestMatchers("/medical-services/create","/medical-services/remove","medical-services/update/{id}").hasAuthority("ADMIN")
                .requestMatchers(HttpMethod.GET,"/patients/single-page","/patients/appointments").hasAnyAuthority("PATIENT")
                .requestMatchers("/patients/update/{id}","/patients/remove","/patients", "/patients/{id}").hasAnyAuthority("ADMIN")
                .requestMatchers( HttpMethod.POST,"/patients/search").hasAnyAuthority("ADMIN")
                .requestMatchers("/patients/update","/patients/delete","/patients").hasAnyAuthority("ADMIN")
                .requestMatchers("/doctors/**").hasAuthority("ADMIN")
                .requestMatchers("/appointments").hasAuthority("ADMIN")
                .requestMatchers("/appointments/to-make").hasAuthority("PATIENT")
                .requestMatchers("/appointments/cancel").hasAnyAuthority("PATIENT","DOCTOR")
                .requestMatchers("/doctors").hasAuthority("ADMIN")
                .requestMatchers("/comment" ,"/comment/delete").hasAnyAuthority("PATIENT","ADMIN")
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
