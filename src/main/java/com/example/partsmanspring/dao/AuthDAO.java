package com.example.partsmanspring.dao;

import com.example.partsmanspring.models.AuthToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AuthDAO extends JpaRepository<AuthToken, Integer> {
    AuthToken findAuthTokenByToken(String token);

    //    AuthToken findAuthTokenByTokenPayload(String tokenPayload);????????
    //all there words are reserved. token and Token must be the same
    //this works bc we tied in USER with fetchtype.eager
    //therefore when token gets called it'll auto tie it to the user

}
