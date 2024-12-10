package com.bombulis.stock.control.config;

import com.bombulis.stock.control.component.filter.St_JwtTokenFilter;
import com.bombulis.stock.control.service.AuthenticationService.St_AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class St_SecurityConfig {

    @Autowired
    private St_AuthenticationService authenticationService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        St_JwtTokenFilter jwtTokenFilter = new St_JwtTokenFilter();
        jwtTokenFilter.setAuthenticationService(authenticationService);
        http.csrf(csrf -> csrf.disable())
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .sessionManagement((e) -> e.sessionCreationPolicy(SessionCreationPolicy.NEVER))
                .authorizeHttpRequests(authorize ->
                        authorize
                                .requestMatchers( "/error").permitAll()
                                .requestMatchers("/**").authenticated()
                                .anyRequest()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                        .permitAll()
                );
        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
