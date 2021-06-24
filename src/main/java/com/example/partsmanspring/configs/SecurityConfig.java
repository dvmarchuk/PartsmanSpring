package com.example.partsmanspring.configs;


import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

//to set up with beans we can use several different @'s
//component, service, controller, repository all do the same thing but its just so you can organize it for yourself
@Configuration//allow me to declare individual beans in the class then i can call them individually
@EnableWebSecurity//to allow web-security adapter
public class SecurityConfig extends WebSecurityConfigurerAdapter { //webSecurityConfigAdap sets up all necessary pieces DB methods
    //It accepts all traffic onto itself. Checks its headers and decides what to do with it.


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication().withUser("dennis").password("{noop}marchuk").roles("ADMIN");//{noop} can be put in before the password to allow a text password instead of a hashcode
        auth.inMemoryAuthentication().withUser("root").password("{noop}root").roles("USER");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.
            csrf().disable().             //we are usinh MVC so we need the csrf
            authorizeRequests().        //this now allows us to have authoized URLs
            antMatchers("/").permitAll().  //this is for which urls we authorize and who we permit
            antMatchers("/users").hasAnyRole("USER", "ADMIN").  //only roles with user and admin permitted
            antMatchers("users/**").hasAnyRole("ADMIN").
            and(). //this is to change types away from authorizeRequests/antMatchers
            httpBasic(). //says we are using database login
            and().
            sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);//for our session not to be saved but we want to log in more often"
    }
}
