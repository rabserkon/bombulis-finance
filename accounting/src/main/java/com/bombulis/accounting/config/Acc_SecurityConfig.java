package com.bombulis.accounting.config;

import com.bombulis.accounting.component.filter.Acc_JwtTokenFilter;
import com.bombulis.accounting.service.AuthenticationService.Acc_AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
@PropertySource("classpath:application-${spring.profiles.active}.properties")
public class Acc_SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private Acc_AuthenticationService authenticationService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        Acc_JwtTokenFilter jwtTokenFilter = new Acc_JwtTokenFilter();
        jwtTokenFilter.setAuthenticationService(authenticationService);
        http.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .cors().disable()
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/v1/auth/**").not().authenticated()
                .antMatchers("/v1/service/**").hasRole("USER")
                .antMatchers("/v1/revaluations/**").hasRole("USER")
                .antMatchers("/v1/accounts/**").hasRole("ACCOUNTS")
                .antMatchers("/v1/sources/**").hasRole("ACCOUNTS")
                .antMatchers("/v1/transaction/**").hasRole("TRANSACTIONS")
                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .anyRequest().authenticated();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }


}
