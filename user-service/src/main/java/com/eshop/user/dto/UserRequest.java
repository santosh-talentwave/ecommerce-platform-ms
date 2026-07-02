package com.eshop.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

@Schema(name = "UserRequest", description = "Payload to create an user.")
public record UserRequest(

        @Schema(example = "John", maxLength = 100, description = "Firstname of the user.")
        @Size(max = 100)
        String firstName,

        @Schema(example = "Smith", maxLength = 100, description = "Lastname of the user.")
        @Size(max = 100)
        String lastName,

        @Schema(example = "user1", maxLength = 100, description = "Username of the user.")
        @NotBlank(message = "Username is required")
        @Size(max = 100)
        String username,

        @Schema(example = "pass1", description = "Password of the user.")
        String password,

        @Schema(example = "john@gmail.com", maxLength = 150, description = "Firstname of the user.")
        @NotBlank
        @Email(message = "Email must be valid")
        @Size(max = 150)
        String email,

        @Schema(example = "2010-01-10", description = "Date of birth of the user.")
        @Past(message = "Enter valid dob.It can be only in past.")
        LocalDate dob,

        @Schema(example = "123456", maxLength = 20, description = "Phone no of the user.")
        @Size(max = 20)
        @Max(value = 999999999, message = "Number exceeded")
        String phoneNo,

        @Schema(example = "Mael/Female", maxLength = 20, description = "Gender of the user.")
        @Size(max = 20)
        String gender,

         @Schema(example = "ROLE_ADMIN", description = "Role name of the user.")
         String roleName
) {

}
