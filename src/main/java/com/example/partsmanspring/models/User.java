package com.example.partsmanspring.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

//@Entity//to add to database
//@NoArgsConstructor
//@Data//for setters
//public class User {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private int user_id;
//    private String name;
//    private String email;
//    private String password;
//    private String role = "USER";
//
//    @JsonIgnore
//    @OneToMany(cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
//    private List<AuthToken> tokenList = new ArrayList<>();
//
//    public User(String name) {
//        this.name = name;
//    }
//}

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"tokenList"})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // ++
    private int user_id;
    private String name;
    private String password;
    private String email;
    private String role = "ROLE_USER";
    private String token;
    private boolean isAuthenticated;
    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<AuthToken> tokenList = new ArrayList<>();

    public User(String name, List<AuthToken> tokenList, boolean isAuthenticated) {
        this.name = name;
        this.tokenList = tokenList;
        this.isAuthenticated = isAuthenticated;

    }


}
