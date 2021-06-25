package com.example.partsmanspring.configs;

import com.example.partsmanspring.dao.AuthDAO;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class RequestsProcessingJWTFilter extends GenericFilterBean {//here we need GenericFilterBean to set up the class to work with LoginFilter and implements all successful authentication methods
//the genericFilterBean is basically the same thing as AbstractAuthenticationProcessingFilter in loginFilter.java
//except in AAPFilter we need to initialize attemptAuthenticaion and here its dofilter

    @Override
    public void doFilter(ServletRequest clientRequest, ServletResponse serverResponse, FilterChain filterChain) throws IOException, ServletException {
        //here we will be pulling out the token from our requests
        System.out.println("RequestsProcessingJWTFilter");

        HttpServletRequest httpServletRequest = (HttpServletRequest) clientRequest;
        //this is here so we can pull the headers out of the ServletRequest. We have to hard cast it into a HttpServletRequest

        String authorizationToken = httpServletRequest.getHeader("Authorization");//taking the header with the name authorization and pulling it out
        authorizationToken.replace("bearer ", "");//to get bearer out of name. idk if this is needed 100%

        System.out.println(authorizationToken);
    }
}
