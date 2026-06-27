package com.eshop.user.controller;

import com.eshop.user.dto.UserRequest;
import com.eshop.user.dto.UserResponse;
import com.eshop.user.dto.UserUpdateRequest;
import com.eshop.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(name = "User Service", description = "APIS for managing user info")
public class UserRestController {

    private final UserService userService;

    @Operation(
            summary = "Create a new user",
            description = "Creates a new user and returns the created user details.",
            responses = {
                    @ApiResponse(
                            responseCode = "201", description = "Created",
                            content = @Content(schema = @Schema(implementation = UserResponse.class))
                    )
            }
    )
    @PostMapping
    public ResponseEntity<?> createUser(@Valid @RequestBody UserRequest request) {
        UserResponse response = userService.createUser(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Updates an user",
            description = "Updates an existing user and returns the updated user details.",
            responses = {
                    @ApiResponse(
                            responseCode = "200", description = "Ok",
                            content = @Content(schema = @Schema(implementation = UserResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "404", description = "Not Found", content = @Content
                    )
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @Valid @RequestBody UserUpdateRequest request) {
        UserResponse response = userService.updateUser(id, request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(
            summary = "Delete an user",
            description = "Delete an user by id.",
            responses = {
                    @ApiResponse(
                            responseCode = "204", description = "No Content",
                            content = @Content(schema = @Schema(implementation = UserResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "404", description = "Not Found", content = @Content
                    )
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(
            summary = "Get user by ID",
            description = "Get an user by id.",
            responses = {
                    @ApiResponse(
                            responseCode = "200", description = "Ok",
                            content = @Content(schema = @Schema(implementation = UserResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "404", description = "Not Found", content = @Content
                    )
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<?> getUser(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @Operation(
            summary = "List User",
            description = "Returns all user with details.",
            responses = {
                    @ApiResponse(
                            responseCode = "200", description = "Ok",
                            content = @Content(
                                    array = @ArraySchema(
                                            schema = @Schema(implementation = UserResponse.class)
                                    )
                            )
                    )
            }
    )
    @GetMapping
    public ResponseEntity<?> getAllUsers() {
        return ResponseEntity.ok(userService.getUsers());
    }
}
