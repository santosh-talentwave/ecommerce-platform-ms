package com.eshop.user.service;

import com.eshop.user.dto.UserRequest;
import com.eshop.user.dto.UserResponse;
import com.eshop.user.dto.UserUpdateRequest;

import java.util.List;

public interface UserService {

    UserResponse createUser(UserRequest request);

    UserResponse updateUser(Long id, UserUpdateRequest request);

    void deleteUser(long id);

    UserResponse getUserById(long id);

    List<UserResponse> getUsers();

    UserResponse getUserByUsername(String username);
}
