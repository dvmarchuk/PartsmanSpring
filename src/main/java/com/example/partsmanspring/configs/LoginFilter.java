package com.example.partsmanspring.configs;

import com.example.partsmanspring.dao.AuthDAO;
import com.example.partsmanspring.dao.UserDAO;
import com.example.partsmanspring.models.AuthToken;
import com.example.partsmanspring.models.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class LoginFilter extends AbstractAuthenticationProcessingFilter {

    private AuthenticationManager authenticationManager;
    private AuthDAO authDAO;//we put this into login filter bc it is not a bean
    private User user;//to let us use it globally in this file
    private UserDAO userDAO;

    public LoginFilter(String defaultFilterProcessesUrl, AuthenticationManager authenticationManager, AuthDAO authDAO, UserDAO userDAO) {
        super(defaultFilterProcessesUrl);
        this.authenticationManager = authenticationManager;
        this.authDAO = authDAO;//this is needed to connect the authDAO we are sent in to the one declared in the file so that we can use it
        this.userDAO = userDAO;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws AuthenticationException, IOException, ServletException {
        System.out.println("login filter authentication");
        //this is responsible in spring for the mapping of JSON objects. Binds requests to models
        this.user = new ObjectMapper().readValue(httpServletRequest.getInputStream(), User.class);//first arg is to get what is coming into the server
        System.out.println(user);
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getName(), user.getPassword()));
        //this checks securityConfig AuthenticationManagerBuilder auth to compare username and password so I don't have to and gives token
        //if auth fails at this level then unsuccessfulAuthentication class runs
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
        //here we use JWT to tokenize our credentials
        String token = Jwts.builder().
                setSubject("CodedWord").//what we are coding
                signWith(SignatureAlgorithm.HS512, "codeword".getBytes()).//code word is our key to undo the Hash
                compact();



        AuthToken authToken = new AuthToken(token);
        userDAO.save(user);//it needs to be done through the DB bc otherwise the user has no id therefore cannot be tied to the token
        authToken.setUser(user);

        //need to save user to DB
        //to do this I need to pass in UserDAO from SecurityConfig
        authDAO.save(authToken);//uses the constructor to pass it to AuthDAO and save it with AuthToken and tie with user in DB



        response.addHeader("Authorization", "Bearer "+ token);
        //this now gets attached to the headers and now gets sent around as authentication




        chain.doFilter(request, response);//this is responsible for chaining together the loginfilter call in security config
        //the request will take in my login and password and log-in


    }
}
