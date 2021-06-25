package com.example.partsmanspring.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;


//@Entity
//@AllArgsConstructor
//@NoArgsConstructor
//@Data
//@ToString(exclude = {"user"})//to not put user in database and stack overflow
//public class AuthToken {
//        @Id
//        @GeneratedValue(strategy = GenerationType.IDENTITY)
//        private int id;
//        private String token;
//        //cascadetype is what to do with the user when we delete the authtoken but have to be careful. makes authtoken not have info about user
//        @JsonIgnore//to not show the connection to user in the database json to prevent stack overflow
//        @ManyToOne(cascade = CascadeType.DETACH, fetch = FetchType.EAGER)//eager will automatically fill the DB with user when authtoken is called if its set to lazy it wont auto fill them
//        private User user;
//
//
//        public AuthToken(String token) {
//                this.token = token;
//        }
//}

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"user"})
public class AuthToken {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private int id;
        private String token;
        @JsonIgnore
        @ManyToOne(fetch = FetchType.EAGER,cascade = CascadeType.DETACH)
        private User user;

        public AuthToken(String token) {
                this.token = token;
        }
}