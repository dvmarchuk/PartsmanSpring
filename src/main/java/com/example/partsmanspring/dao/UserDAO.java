package com.example.partsmanspring.dao;

import com.example.partsmanspring.models.AuthToken;
import com.example.partsmanspring.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserDAO extends JpaRepository<User, Integer> {
    //    @Query("select u from User u where u.username=:username")
    User findUserByName(String name);
    User findUserByToken(String token);
    //these names are not optional. This communicates with the server.
    //findAuthTokenByUser_User_Id is all reserved and int user_id is users keyword
}
