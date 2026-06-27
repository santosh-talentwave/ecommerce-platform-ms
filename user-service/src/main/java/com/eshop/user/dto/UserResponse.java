package com.eshop.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

public record UserResponse (
        @Schema(example = "1",  description = "Id of the user.")
        Long id,

        @Schema(example = "John",  description = "Firstname of the user.")
        String firstName,

        @Schema(example = "John", description = "Lastname of the user.")
        String lastName,

        @Schema(example = "user1", description = "Username of the user.")
        String username,

        @Schema(example = "pass1",  description = "Password of the user.")
        String password,

        @Schema(example = "john@gmail.com",  description = "Email of the user.")
        String email,

        @Schema(example = "2021-01-10",  description = "Date of birth of the user.")
        LocalDate dob,

        @Schema(example = "123456",  description = "Phone no of the user.")
        String phoneNo,

        @Schema(example = "Mal/Female", description = "Gender of the user.")
        String gender
) {
}
