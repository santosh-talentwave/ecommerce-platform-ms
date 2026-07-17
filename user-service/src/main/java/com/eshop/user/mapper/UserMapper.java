package com.eshop.user.mapper;

import com.eshop.user.dto.UserRequest;
import com.eshop.user.dto.UserResponse;
import com.eshop.user.dto.UserUpdateRequest;
import com.eshop.user.model.Role;
import com.eshop.user.model.User;

public final class UserMapper {

    private UserMapper() {
    }

    public static User toEntity(UserRequest userRequest) {
        return User.builder()
                .firstName(userRequest.firstName())
                .lastName(userRequest.lastName())
                .email(userRequest.email())
                .password(userRequest.password())
                .dob(userRequest.dob())
                .phoneNo(userRequest.phoneNo())
                .gender(userRequest.gender())
                .username(userRequest.username())
                .build();
    }

    public static UserResponse toResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getUsername(),
                user.getPassword(),
                user.getEmail(),
                user.getDob(),
                user.getPhoneNo(),
                user.getGender(),
                user.getRoles().stream().map(Role::getName).findFirst().orElse(null)
        );
    }

    public static User toEntity(User user, UserUpdateRequest userUpdateRequest) {
        if (userUpdateRequest.firstName() != null) user.setFirstName(userUpdateRequest.firstName());
        if (userUpdateRequest.lastName() != null) user.setLastName(userUpdateRequest.lastName());
        if (userUpdateRequest.email() != null) user.setEmail(userUpdateRequest.email());
        if (userUpdateRequest.phoneNo() != null) user.setPhoneNo(userUpdateRequest.phoneNo());
        if (userUpdateRequest.dob() != null) user.setDob(userUpdateRequest.dob());
        return user;
    }
}
