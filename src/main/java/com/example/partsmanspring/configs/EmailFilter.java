package com.example.partsmanspring.configs;

import com.example.partsmanspring.dao.AuthDAO;
import com.example.partsmanspring.dao.UserDAO;
import com.example.partsmanspring.models.AuthToken;
import com.example.partsmanspring.models.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class EmailFilter extends AbstractAuthenticationProcessingFilter {

    private AuthenticationManager authenticationManager;
    private AuthDAO authDAO;//we put this in bc it is not a bean
    private User user;//to let us use it globally in this file
    private AuthToken authToken;
    private UserDAO userDAO;

    public EmailFilter(String defaultFilterProcessesUrl, AuthenticationManager authenticationManager, AuthDAO authDAO, UserDAO userDAO) {
        super(defaultFilterProcessesUrl);
        this.authenticationManager = authenticationManager;
        this.authDAO = authDAO;//this is needed to connect the authDAO we are sent in to the one declared in the file so that we can use it
        this.userDAO = userDAO;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws AuthenticationException, IOException, ServletException {
        System.out.println("email filter authentication");

        //this is responsible in spring for the mapping of JSON objects. Binds requests to models
        this.user = new ObjectMapper().readValue(httpServletRequest.getInputStream(), User.class);//first arg is to get what is coming into the server
        System.out.println(user);
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getName(), user.getPassword()));
        //this checks securityConfig AuthenticationManagerBuilder auth to compare username and password so I don't have to and gives token
        //if auth fails at this level then unsuccessfulAuthentication class runs
        System.out.println("authenticated " + authenticate);
        return authenticate;
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        System.out.println("Auth Failed");
        super.unsuccessfulAuthentication(request, response, failed);
    }

    @Override
    protected void successfulAuthentication(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain,
            Authentication authResult) throws IOException, ServletException {
        //super.successfulAuthentication(request, response, chain, authResult);






    }
}
