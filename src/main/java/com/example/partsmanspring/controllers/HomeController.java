package com.example.partsmanspring.controllers;

import com.example.partsmanspring.dao.UserDAO;
import com.example.partsmanspring.models.User;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
@CrossOrigin(origins = {"http://localhost:4200"})
public class HomeController {

    private UserDAO userDAO;//I could also put autowired on top if needed but instead im using AllArgsConstructor


    @GetMapping("/users")
    public List<User> getUsers(){
        return userDAO.findAll();
    }

    @GetMapping("/users/{id}")
    public Optional<User> getUserById(@PathVariable int id){ //pathvariable connects the int id and the id in {} to say theyre related. You can also add after pathvariable to make a pseudonym
        return userDAO.findById(id);
        //i can add .get() to end of return statement to do without Optional
    }

    @DeleteMapping("/users/{id}")//this one /users/4
    public List<User> deleteUsersById(@PathVariable int id){
        userDAO.deleteById(id);
        return userDAO.findAll();
    }

//    @DeleteMapping("/users")//localhost:8080/users?id=4
//    public List<User> deleteUsersById(@RequestParam int id){
//        userDAO.deleteById(id);
//        return userDAO.findAll();
//    }

    @PostMapping("/users")
    public void postUsers(@RequestBody User user){ //the requestbody makes the body come into the object user
        userDAO.save(user);
    }

    @GetMapping("/")
    public String home(){
        return "welcome home";
    }

    @GetMapping("/about")
    public String about(){
        return "partsman for TTR";
    }

}
