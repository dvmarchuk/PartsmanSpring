package com.example.partsmanspring.controllers;

import com.example.partsmanspring.dao.userDAO;
import com.example.partsmanspring.models.User;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
public class HomeController {

    private userDAO userDAO;//I could also put autowired on top if needed but instead im using AllArgsConstructor


    @GetMapping("/users")
    public List<User> getUsers(){
        return userDAO.findAll();
    }

    @GetMapping("/users/{id}")
    public Optional<User> getUserById(@PathVariable int id){ //pathvariable connects the int id and the id in {} to say theyre related. You can also add after pathvariable to make a pseudonym
        return userDAO.findById(id);
        //i can add .get() to end of return statement to do without Optional
    }

    @DeleteMapping("/users/{id}")
    public List<User> deleteUsersById(@PathVariable int id){
        userDAO.deleteById(id);
        return userDAO.findAll();
    }

    @PostMapping("/users")
    public void postUsers(@RequestBody User user){ //the requestbody makes the body come into the object user
        userDAO.save(user);
    }
}
