package com.vicarius.quotas.service;

import com.vicarius.quotas.model.User;

import java.util.List;



import java.util.List;

public interface UserService {
    User saveUser(User user);
    List<User> getAllUsers();
    User getUserById(String id);
    User updateUser(User user, String id);
    void deleteUser(String id);
    List<User> getUsersQuota();
}
