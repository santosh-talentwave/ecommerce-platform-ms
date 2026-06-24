package com.eshop.user.dto;

import java.time.LocalDate;

public record UserUpdateRequest (
        String firstName,

        String lastName,

        String email,

        LocalDate dob,

        String phoneNo

){
}
