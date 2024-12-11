package com.social.network.authentication.module.config;

import com.social.network.authentication.module.component.filter.JwtAuthenticationFilter;
import com.social.network.authentication.module.component.filter.LogoutFilter;
import com.social.network.authentication.module.service.AuthenticationService.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@PropertySource("classpath:application-${spring.profiles.active}.properties")
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private AuthenticationService authenticationService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter();
        jwtAuthenticationFilter.setAuthenticationService(authenticationService);
        http.addFilterAt(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterAt(new LogoutFilter(), org.springframework.security.web.authentication.logout.LogoutFilter.class)
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .cors().disable()
                .csrf().disable()
                .authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .antMatchers("/registration").not().authenticated()
                .antMatchers("/registration/confirm").not().authenticated()
                .antMatchers(HttpMethod.POST, "/validate/user").permitAll()
                .antMatchers(HttpMethod.POST, "/credential/update", "/credential/reset", "/validate/token").permitAll()
                .antMatchers("/service/status", "/get/recaptcha/key").permitAll()
                .antMatchers("/registration/token/confirm").permitAll()
                .antMatchers("/login").not().authenticated()
                .anyRequest().authenticated()
                .and().logout().logoutUrl("/logout/0");
    }


    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    public void setAuthenticationService(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }
}
