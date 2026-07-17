package com.eshop.user.service;

import com.eshop.user.dto.UserRequest;
import com.eshop.user.dto.UserResponse;
import com.eshop.user.dto.UserUpdateRequest;
import com.eshop.user.exception.UserNotFoundException;
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
import java.util.List;
import java.util.Optional;

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

        UserResponse userResponseActual = userService.createUser(userRequest);

        //Assertion
        Assertions.assertNotNull(userResponseActual);
        Assertions.assertEquals(userResponseExpected, userResponseActual);
        Assertions.assertEquals(userResponseExpected.id(), userResponseActual.id());
        Assertions.assertEquals(userResponseExpected.username(), userResponseActual.username());
        Assertions.assertEquals(userResponseExpected.email(), userResponseActual.email());

    }

    @Test
    public void testUpdateUser() {
        //Arrange
        long id = 1;
        UserUpdateRequest userRequest = createUpdateUserRequest();
        User userFromDb = createUser();
        User user = UserMapper.toEntity(userFromDb, userRequest);
        UserResponse userResponseExpected = createUserResponse();

        //Act
        //mock
        Mockito.when(userRepository.findById(id)).thenReturn(Optional.of(userFromDb));
        Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(user);

        UserResponse userResponseActual = userService.updateUser(id, userRequest);

        //Assertion
        Assertions.assertNotNull(userResponseActual);
        Assertions.assertEquals(userResponseExpected, userResponseActual);
        Assertions.assertEquals(userResponseExpected.id(), userResponseActual.id());
        Assertions.assertEquals(userResponseExpected.username(), userResponseActual.username());
        Assertions.assertEquals(userResponseExpected.email(), userResponseActual.email());

    }

    @Test
    public void testUpdateUserWhenUserNotFound() {
        //Arrange
        long id = 1;
        UserUpdateRequest userRequest = createUpdateUserRequest();

        //Act
        //mock
        Mockito.when(userRepository.findById(id)).thenReturn(Optional.empty());

        UserNotFoundException ex = Assertions.assertThrows(UserNotFoundException.class,
                () -> userService.updateUser(id, userRequest));

        //Assertion
        Assertions.assertEquals("User not found with id " + id, ex.getMessage());

        Mockito.verify(userRepository, Mockito.times(1)).findById(id);
        Mockito.verify(userRepository, Mockito.never()).save(Mockito.any(User.class));
        Mockito.verifyNoMoreInteractions(userRepository);
    }

    @Test
    public void testDeleteUser() {
        long id = 1;

        //mock
        Mockito.when(userRepository.existsById(id)).thenReturn(true);
        Mockito.doNothing().when(userRepository).deleteById(id);

        userService.deleteUser(id);

        Mockito.verify(userRepository, Mockito.times(1)).existsById(id);
        Mockito.verify(userRepository, Mockito.times(1)).deleteById(id);
        Mockito.verifyNoMoreInteractions(userRepository);
    }

    @Test
    public void testDeleteUserWhenUserNotFound() {
        //Arrange
        long id = 1;

        //Act
        //mock
        Mockito.when(userRepository.existsById(id)).thenReturn(false);

        UserNotFoundException ex = Assertions.assertThrows(UserNotFoundException.class,
                () -> userService.deleteUser(id));

        //Assertion
        Assertions.assertEquals("User not found with id " + id, ex.getMessage());

        Mockito.verify(userRepository, Mockito.times(1)).existsById(id);
        Mockito.verify(userRepository, Mockito.never()).deleteById(id);
        Mockito.verifyNoMoreInteractions(userRepository);
    }

    @Test
    public void testGetUser() {
        long id = 1;
        User userFromDb = createUser();
        UserResponse userResponseExpected = createUserResponse();

        //Act
        //mock
        Mockito.when(userRepository.findById(id)).thenReturn(Optional.of(userFromDb));

        UserResponse userResponseActual = userService.getUserById(id);

        //Assertion
        Assertions.assertNotNull(userResponseActual);
        Assertions.assertEquals(userResponseExpected, userResponseActual);
        Assertions.assertEquals(userResponseExpected.id(), userResponseActual.id());
        Assertions.assertEquals(userResponseExpected.username(), userResponseActual.username());
        Assertions.assertEquals(userResponseExpected.email(), userResponseActual.email());
    }

    @Test
    public void testGetUserWhenUserNotFound() {
        //Arrange
        long id = 1;

        //Act
        //mock
        Mockito.when(userRepository.findById(id)).thenReturn(Optional.empty());

        UserNotFoundException ex = Assertions.assertThrows(UserNotFoundException.class,
                () -> userService.getUserById(id));

        //Assertion
        Assertions.assertEquals("User not found with id " + id, ex.getMessage());

        Mockito.verify(userRepository, Mockito.times(1)).findById(id);
        Mockito.verifyNoMoreInteractions(userRepository);
    }


    @Test
    public void testGetAllUsers() {
        User userFromDb = createUser();
        UserResponse userResponseExpected = createUserResponse();

        Mockito.when(userRepository.findAll()).thenReturn(List.of(userFromDb));

        List<UserResponse> userResponseActuals = userService.getUsers();

        Assertions.assertNotNull(userResponseActuals);
        UserResponse userResponseActual = userResponseActuals.getFirst();
        Assertions.assertEquals(userResponseExpected.id(), userResponseActual.id());
        Assertions.assertEquals(userResponseExpected.username(), userResponseActual.username());
        Assertions.assertEquals(userResponseExpected.email(), userResponseActual.email());
    }

    @Test
    public void testGetAllUserWhenUserNotFound() {
        //Arrange
        long id = 1;

        //Act
        //mock
        Mockito.when(userRepository.findAll()).thenReturn(List.of());

        UserNotFoundException ex = Assertions.assertThrows(UserNotFoundException.class,
                () -> userService.getUsers());

        //Assertion
        Assertions.assertEquals("User not found", ex.getMessage());

        Mockito.verify(userRepository, Mockito.times(1)).findAll();
        Mockito.verifyNoMoreInteractions(userRepository);
    }


    private User createUser() {
        User user = new User();
        user.setId(1L);
        user.setFirstName("John");
        user.setLastName("Smith");
        user.setUsername("user1");
        user.setPassword("pass1");
        user.setEmail("john@gmail.com");
        user.setDob(LocalDate.of(2000, 10, 1));
        user.setPhoneNo("99999");
        user.setGender("Male");
        return user;
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
                "Male",
                "ADMIN_ROLE"
        );
    }

    private UserUpdateRequest createUpdateUserRequest() {
        return new UserUpdateRequest(
                "John",
                "Smith",
                "john@gmail.com",
                LocalDate.of(2000, 10, 1),
                "99999",
                "",
                "ADMIN_ROLE"
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
                "Male",
                "ADMIN_ROLE"
        );
    }
}
