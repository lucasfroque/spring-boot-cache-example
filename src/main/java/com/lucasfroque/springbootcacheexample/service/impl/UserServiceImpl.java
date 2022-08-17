package com.lucasfroque.springbootcacheexample.service.impl;

import com.lucasfroque.springbootcacheexample.model.User;
import com.lucasfroque.springbootcacheexample.repository.UserRepository;
import com.lucasfroque.springbootcacheexample.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository repository;

    @Override
    public User save(User user) {
        return repository.save(user);
    }

    @Override
    public User update(Long id, User user) {
        User userToUpdate = repository.findById(id).get();
            userToUpdate.setName(user.getName());
            userToUpdate.setEmail(user.getEmail());
            return repository.save(userToUpdate);
    }

    @Override
    public List<User> findAll() {
        return repository.findAll();
    }

    @Override
    public User findById(Long id) {
        return repository.findById(id).get();
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }
}
