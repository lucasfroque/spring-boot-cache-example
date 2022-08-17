package com.lucasfroque.springbootcacheexample.repository;

import com.lucasfroque.springbootcacheexample.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
