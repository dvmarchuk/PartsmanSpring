package com.example.partsmanspring.dao;

import com.example.partsmanspring.models.AuthToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public interface AuthDAO extends JpaRepository<AuthToken, Integer> {
}
