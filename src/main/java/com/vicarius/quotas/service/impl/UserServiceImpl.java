package com.vicarius.quotas.service.impl;

import com.vicarius.quotas.model.User;
import com.vicarius.quotas.repository.UserRepository;
import com.vicarius.quotas.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public List<User> getAllUsers() {
        return (List<User>) userRepository.findAll();
    }

    @Override
    public User getUserById(String id) {
        return userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
    }

    @Override
    public User updateUser(User user, String id) {
        User existingUser = getUserById(id);
        if (existingUser != null) {
            existingUser.setFirstName(user.getFirstName());
            existingUser.setLastName(user.getLastName());
            existingUser.setLastLoginTimeUtc(user.getLastLoginTimeUtc());
            existingUser.setQuota(user.getQuota());
            userRepository.save(existingUser);
        }
        return existingUser;
    }

    @Override
    public void deleteUser(String id) {
        User existingUser = getUserById(id);
        if (existingUser != null) {
            userRepository.deleteById(id);
        }
    }

    @Override
    public List<User> getUsersQuota() {
        return (List<User>) userRepository.findAll();
    }
}

