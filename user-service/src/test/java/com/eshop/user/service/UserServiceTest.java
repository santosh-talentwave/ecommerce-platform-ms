package com.eshop.user.service;

import com.eshop.user.dto.UserRequest;
import com.eshop.user.dto.UserResponse;
import com.eshop.user.mapper.UserMapper;
import com.eshop.user.model.User;
import com.eshop.user.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    public void testCreateUser() {
        //Arrange
        UserRequest userRequest = createUserRequest();
        User user = UserMapper.toEntity(userRequest);
        user.setId(1L);
        UserResponse userResponseExpected = createUserResponse();

        //Act
        //mock
        Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(user);

        UserResponse  userResponseActual = userService.createUser(userRequest);

        //Assertion
        Assertions.assertNotNull(userResponseActual);
        Assertions.assertEquals(userResponseExpected, userResponseActual);
        Assertions.assertEquals(userResponseExpected.id(), userResponseActual.id());
        Assertions.assertEquals(userResponseExpected.username(), userResponseActual.username());
        Assertions.assertEquals(userResponseExpected.email(), userResponseActual.email());

    }

    private UserRequest createUserRequest() {
        return new UserRequest(
                "John",
                "Smith",
                "user1",
                "pass1",
                "john@gmail.com",
                LocalDate.of(2000, 10, 1),
                "99999",
                "Male"
        );
    }

    private UserResponse createUserResponse() {
        return new UserResponse(
                1L,
                "John",
                "Smith",
                "user1",
                "pass1",
                "john@gmail.com",
                LocalDate.of(2000, 10, 1),
                "99999",
                "Male"
        );
    }
}
