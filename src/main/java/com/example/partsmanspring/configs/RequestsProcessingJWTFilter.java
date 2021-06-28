package com.example.partsmanspring.configs;

import com.example.partsmanspring.dao.AuthDAO;
import com.example.partsmanspring.dao.UserDAO;
import com.example.partsmanspring.models.AuthToken;
import com.example.partsmanspring.models.User;
import io.jsonwebtoken.Jwts;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class RequestsProcessingJWTFilter extends GenericFilterBean {//here we need GenericFilterBean to set up the class to work with LoginFilter and implements all successful authentication methods
//the genericFilterBean is basically the same thing as AbstractAuthenticationProcessingFilter in loginFilter.java
//except in AAPFilter we need to initialize attemptAuthenticaion and here its dofilter

    private AuthDAO authDAO;

    public RequestsProcessingJWTFilter(AuthDAO authDAO) {
        this.authDAO = authDAO;
    }

    @Override
    public void doFilter(ServletRequest clientRequest, ServletResponse serverResponse, FilterChain filterChain) throws IOException, ServletException {
        //here we will be pulling out the token from our requests
        System.out.println("RequestsProcessingJWTFilters");

        HttpServletRequest httpServletRequest = (HttpServletRequest) clientRequest;
        //this is here so we can pull the headers out of the ServletRequest. We have to hard cast it into a HttpServletRequest
        String authorizationToken = httpServletRequest.getHeader("Authorization");//taking the header with the name authorization and pulling it out
        //could i take this AuthorizationToken and put it into my email?


        if(authorizationToken != null && authorizationToken.startsWith("Bearer")){
            String bearerToken = authorizationToken.replace("Bearer ", "");//to get bearer out of name. idk if this is needed 100%
            System.out.println("got here");

            AuthToken authTokenByToken = authDAO.findAuthTokenByToken(bearerToken);

            User user = authTokenByToken.getUser();
            String username = user.getName();
            String password = user.getPassword();

            SimpleGrantedAuthority role = new SimpleGrantedAuthority(user.getRole());
            List<SimpleGrantedAuthority> simpleGrantedAuthorities = Arrays.asList(role);//we have to put it into an array

            //after taking out username and password we send it to UserPassAuthTok method and it compares the username and password by itself
            Authentication authentication = new UsernamePasswordAuthenticationToken(username, password, simpleGrantedAuthorities);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            System.out.println(SecurityContextHolder.getContext());








            //BUT WE WILL USE ANOTHER METHOD AS THIS IS AN EXAMPLE BUT IS UNSECURE
//            String subject = Jwts.parser()//same class as the jwts builder in login filter
//                    .setSigningKey("codeword".getBytes())//key word
//                    .parseClaimsJws(authorizationToken)//what we parse
//                    .getBody()//to get body
//                    .getSubject();//get what we wrote since we wrote in subject



        }

        filterChain.doFilter(clientRequest,serverResponse);//to pass the info back to SecurityConfig

    }
}
