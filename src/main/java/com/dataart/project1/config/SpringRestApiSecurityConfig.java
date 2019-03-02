package com.dataart.project1.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@Order(99)
public class SpringRestApiSecurityConfig extends WebSecurityConfigurerAdapter {

    private AuthenticationProvider authProvider;

    @Autowired
    public SpringRestApiSecurityConfig(AuthProvider provider) {
        this.authProvider = provider;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder builder) {
        builder.authenticationProvider(this.authProvider);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .requiresChannel().anyRequest().requiresSecure()
                .and()
                .antMatcher("/api/**")
                .authorizeRequests()
                .anyRequest()
                .hasAuthority("APIUSER")
                .and()
                .httpBasic()
                .and()
                .csrf()
                .disable();
    }


}
