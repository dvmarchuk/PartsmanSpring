package com.example.partsmanspring.controllers;

import com.example.partsmanspring.dao.AuthDAO;
import com.example.partsmanspring.dao.UserDAO;
import com.example.partsmanspring.models.AuthToken;
import com.example.partsmanspring.models.User;
import com.example.partsmanspring.services.MailService;
import io.jsonwebtoken.Jwts;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;
import java.util.Optional;


@RestController
@AllArgsConstructor
@CrossOrigin(origins = {"http://localhost:4200"})
public class UserController {

    private UserDAO userDAO;//I could also put autowired on top if needed but instead im using AllArgsConstructor
    private AuthDAO authDAO;
    private MailService mailService;
//    Authentication authentication;


    @GetMapping("/users")
    public List<User> getUsers() {
        return userDAO.findAll();
    }

    @GetMapping("/users/{id}")
    public Optional<User> getUserById(@PathVariable int id) { //pathvariable connects the int id and the id in {} to say theyre related. You can also add after pathvariable to make a pseudonym
        return userDAO.findById(id);
        //i can add .get() to end of return statement to do without Optional
    }

    @DeleteMapping("/users/{id}")//this one /users/4
    public List<User> deleteUsersById(@PathVariable int id) {
        userDAO.deleteById(id);
        return userDAO.findAll();
    }

    //or use this
//    @DeleteMapping("/users")//localhost:8080/users?id=4
//    public List<User> deleteUsersById(@RequestParam int id) {
//        userDAO.deleteById(id);
//        return userDAO.findAll();
//    }

    @PostMapping("/users")
    public void postUsers(@RequestBody User user) {
        //the requestbody makes the body from the post request come into the object "user"
        userDAO.save(user);

    }

    //these were used for email authentication of my token
    @PostMapping("/registration")
    public void postEmail(@RequestBody User user) {
        String compact = Jwts.builder().setSubject(user.getName()).compact();
        user.setToken(compact);
        userDAO.save(user);
        mailService.send(user);
    }

    @GetMapping("/activate/{token}")
    public void getToken(@PathVariable String token) {
        User user = userDAO.findUserByToken(token);
        String dbToken = user.getToken();
        if (token.equals(dbToken)) {
                userDAO.findUserByToken(token).setAuthenticated(true);
                userDAO.save(user);
        }
    }
}


