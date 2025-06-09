package com.example.evimfix.wpAdmin.Repositories;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import com.example.evimfix.wpAdmin.Models.Auth;
import com.example.evimfix.wpAdmin.Models.Role;

public interface AuthRepository {
    Optional<Auth> getUser(String email);
    HashMap<Integer, String> getUserTest();
    Optional<Auth> getUser(int id);
    List<Role> getRollar();
    List<Auth> getUsers();

    String addUser(Auth auth);
    String updateProfile(Auth auth);
    // Admin terefinden
    String updateUser(Auth auth);
    String deleteUser(String username);
}
