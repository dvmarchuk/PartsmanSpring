package com.example.partsmanspring.configs;


import com.example.partsmanspring.dao.AuthDAO;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

//to set up with beans we can use several different @'s
//component, service, controller, repository all do the same thing but its just so you can organize it for yourself
@Configuration//allow me to declare individual beans in the class then i can call them individually
@EnableWebSecurity//to allow web-security adapter
@AllArgsConstructor//for the authDAO methods
public class SecurityConfig extends WebSecurityConfigurerAdapter { //webSecurityConfigAdap sets up all necessary pieces DB methods
    //It accepts all traffic onto itself. Checks its headers and decides what to do with it.


    private AuthDAO authDAO;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication().withUser("dennis").password("{noop}marchuk").roles("ADMIN");//{noop} can be put in before the password to allow a text password instead of a hashcode
        auth.inMemoryAuthentication().withUser("root").password("{noop}root").roles("USER");
    }

        @Bean
        public CorsConfigurationSource corsConfigurationSource() {
            CorsConfiguration configuration = new CorsConfiguration();
            configuration.setAllowedOrigins(Arrays.asList("http://localhost:4200", "http://localhost:3000"));//for react and angular default servers
            configuration.addAllowedHeader("*");//we allow all headers or we can add certain/multiple headers
            configuration.setAllowedMethods(Arrays.asList(//set up as enum
                    HttpMethod.GET.name(),
                    HttpMethod.HEAD.name(),
                    HttpMethod.POST.name(),
                    HttpMethod.PUT.name(),
                    HttpMethod.DELETE.name()));
            configuration.addExposedHeader("Authorization");//to make sure we can fetch this authorization header in angular or react or whatever when it is needed for us

            UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
            source.registerCorsConfiguration("/**", configuration);//the ** means we will react to all URLs and implement the above configuration
            //source.registerCorsConfiguration("/login", configuration2); //i can set up multiple to work with different configurations
            return source;
        }
            //make sure you have @Configuration at the top of the method for @bean




        @Override
            protected void configure(HttpSecurity http) throws Exception {//with this we take all requests that come to us and using (look two lines down)
                http.
                        cors().configurationSource(corsConfigurationSource())//this line we will push it through CorsConfigSource to check authorizations and if we are sending all that, that we are allowed to send
                        .and()
                        .csrf()
                        .disable()
                        .authorizeRequests()
                        .antMatchers("/", "/save").permitAll()
                        .antMatchers(HttpMethod.POST, "/save").permitAll()
                        .antMatchers(HttpMethod.POST, "/login").permitAll()
                        .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .antMatchers("/users").hasAnyRole("USER", "ADMIN")
                        .and()
                        //we filter the api/login requests
                        //And filter the other requests to make sure of JWT in header
                        .addFilterBefore(new LoginFilter("/login", authenticationManager(), authDAO),UsernamePasswordAuthenticationFilter.class)
                        //this filter out certain url to run before it then passes it into the second arg (UsernamePasswordAuthenticationFilter.class)
                        //this then uses filter chain in LoginFilter to chain the info from LoginFilter to UserPassAuthFilt
                        //authenticationManager() is an object of type authentication manager that will be able to take in credentials and process them through with spring. This is for use in the loginFilter to return back an Authentication object
                        //it is used to complete the loginFilter with the info from loginfilter.java
                        //we then give authDAO through here so we can use it in the loginfilter to save it to the DB
                        //We use addFilterBefore to add the LF filter request before UserPassAuthFilter to receive the token

                        .addFilterBefore(new RequestsProcessingJWTFilter(), UsernamePasswordAuthenticationFilter.class)
                        //this now comes after LoginFilter. After LF has did its job and added the token to the header we can now
                        //send stuff back and forth through all urls (that is why we did not add a url filter here) with
                        //the token that we received. This part has to come after LF.
                        //RequestsProcessJWTFilt always gets activated
                        //We do all requests with token after LF. RePrJWTFilter handles every url even login too


                        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
            }



//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http.
//            csrf().disable().             //we are usinh MVC so we need the csrf
//            authorizeRequests().        //this now allows us to have authoized URLs
//            antMatchers("/").permitAll().  //this is for which urls we authorize and who we permit
//            antMatchers("/users").hasAnyRole("USER", "ADMIN").  //only roles with user and admin permitted
//            antMatchers("users/**").hasAnyRole("ADMIN").
//            and(). //this is to change types away from authorizeRequests/antMatchers
//            httpBasic(). //says we are using database login
//            and().
//            sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);//for our session not to be saved but we want to log in more often"
//    }
}
