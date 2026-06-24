package com.eshop.user.dto;

import java.time.LocalDate;

public record UserRequest(

        String firstName,

        String lastName,

        String username,

        String password,

        String email,

        LocalDate dob,

        String phoneNo,

        String gender
) {

}
