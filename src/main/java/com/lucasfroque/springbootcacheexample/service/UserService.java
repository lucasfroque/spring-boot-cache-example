package com.lucasfroque.springbootcacheexample.service;

import com.lucasfroque.springbootcacheexample.model.User;

import java.util.List;

public interface UserService {
    User save(User user);
    User update(Long id, User user);
    List<User> findAll();
    User findById(Long id);
    void delete(Long id);
}
