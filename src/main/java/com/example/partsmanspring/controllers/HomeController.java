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


    @GetMapping("/")
    public String home(){
        return "welcome home at /";
    }

    @GetMapping("/about")
    public String about(){
        return "partsman for TTR";
    }

}
