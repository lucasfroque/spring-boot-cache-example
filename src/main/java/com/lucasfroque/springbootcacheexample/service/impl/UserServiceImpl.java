package com.lucasfroque.springbootcacheexample.service.impl;

import com.lucasfroque.springbootcacheexample.model.User;
import com.lucasfroque.springbootcacheexample.repository.UserRepository;
import com.lucasfroque.springbootcacheexample.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    public static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserRepository repository;

    @Override
    @CacheEvict(value = "users", allEntries = true)
    public User save(User user) {
        logger.info("Saving user: {}", user.getName());
        return repository.save(user);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "users", allEntries = true),
            @CacheEvict(value = "user", key = "#user.id")
    })
    public User update(Long id, User user) {
        User userToUpdate = repository.findById(id).get();
            userToUpdate.setName(user.getName());
            userToUpdate.setEmail(user.getEmail());

            logger.info("Updating user: {}", userToUpdate.getName());
            return repository.save(userToUpdate);
    }

    @Override
    @Cacheable(value = "users")
    public List<User> findAll() {
        logger.info("Finding all users");
        return repository.findAll();
    }

    @Override
    @Cacheable(value = "user", key = "#id", unless = "#result == null")
    public User findById(Long id) {
        logger.info("Finding user by id: {}", id);
        return repository.findById(id).orElse(null);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "users", allEntries = true),
            @CacheEvict(value = "user", key = "#id")
    })
    public void delete(Long id) {
        logger.info("Deleting user by id: {}", id);
        repository.deleteById(id);
    }
}
