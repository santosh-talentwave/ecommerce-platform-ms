package com.eshop.user.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "user_tbl")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="first_name",  nullable = false, length = 100)
    private String firstName;

    @Column(name="last_name",  nullable = false, length = 100)
    private String lastName;

    @Column(unique = true,  nullable = false, length = 100)
    private String username;

    @Column(  nullable = false)
    private String password;

    @Column( nullable = false, unique = true,  length = 150)
    private String email;

    private LocalDate dob;

    @Column(name = "phone_no", length = 20)
    private String phoneNo;

    @Column(length = 20)
    private String gender;
}
