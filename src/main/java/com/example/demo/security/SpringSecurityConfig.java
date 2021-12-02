package com.example.demo.security;

import com.example.demo.filter.CustomAuthorizationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {


    @Value("${spring.security.user.name}")
    private String userName;

    @Value("${spring.security.password}")
    private String userPassword;

    @Autowired
    UserDetailsService userDetailsService;

    @Autowired
    CustomAuthorizationFilter customAuthorizationFilter;

    //Basic authentication
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {    //configureGlobal
        auth.inMemoryAuthentication()
                .withUser(userName).password(encodePWD().encode(userPassword))
                .authorities("ROLE_USER");

    }

    //encoding password
    @Bean
    public BCryptPasswordEncoder encodePWD() {
        return new BCryptPasswordEncoder();
    }

    //security based on URL
    //JWT authentication for GET and DELETE requests
    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {   //authorization

        httpSecurity.csrf().disable()
                .authorizeRequests().antMatchers("/api/login").permitAll()
                .antMatchers("/api/user").permitAll()
                .anyRequest().authenticated()

                .and().exceptionHandling().and().sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        httpSecurity.addFilterBefore(customAuthorizationFilter, UsernamePasswordAuthenticationFilter.class);

   //basic authentication implementation
       /* httpSecurity.csrf().disable();
        httpSecurity.authorizeRequests()
                .antMatchers("/api/login").permitAll()
                .antMatchers("/api/user").permitAll()
                .anyRequest().authenticated()
                .and()
                .httpBasic()
                .realmName("SpringBootApplication");*/

    }
}
