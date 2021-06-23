package com.example.partsmanspring.dao;

import com.example.partsmanspring.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface userDAO extends JpaRepository<User, Integer> {
}
