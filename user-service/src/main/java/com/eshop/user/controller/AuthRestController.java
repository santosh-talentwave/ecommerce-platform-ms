package com.eshop.user.controller;

import com.eshop.user.dto.AuthRequest;
import com.eshop.user.dto.AuthResponse;
import com.eshop.user.dto.UserRequest;
import com.eshop.user.dto.UserResponse;
import com.eshop.user.service.JwtService;
import com.eshop.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthRestController {

    private final UserService userService;

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    @PostMapping("/token")
    public ResponseEntity<?> authenticateAndGenerateToken(@RequestBody AuthRequest authRequest) {
        // Implementation for authentication and token generation
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.username(), authRequest.password())
        );
        if(authentication.isAuthenticated()) {
            String token = jwtService.generateToken(authRequest.username());
            UserResponse userResponse = userService.getUserByUsername(authRequest.username());
            return ResponseEntity.ok(new AuthResponse(token, userResponse));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @PostMapping("/signup")
    public ResponseEntity<UserResponse> registerUser(@RequestBody @Valid UserRequest userRequest) {
        UserResponse userResponse = userService.createUser(userRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(userResponse);
    }
}
