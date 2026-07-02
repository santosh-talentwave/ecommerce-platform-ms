package com.eshop.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record UserUpdateRequest (
        @Size(max = 100)
        String firstName,

        @Size(max = 100)
        String lastName,

        @Email(message = "Email must be valid")
        @Size(max = 150)
        String email,

        @Past(message = "Enter valid dob.It can be only in past.")
        LocalDate dob,

        @Size(max = 20)
        @Max(value = 9999999, message = "Number exceeded")
        String phoneNo,

        @Schema(example = "pass1", description = "Password of the user.")
        String password,

        @Schema(example = "ROLE_ADMIN", description = "Role name of the user.")
        String roleName

){
}
