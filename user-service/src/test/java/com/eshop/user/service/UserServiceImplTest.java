package com.eshop.user.service;

import com.eshop.user.dto.UserRequest;
import com.eshop.user.dto.UserResponse;
import com.eshop.user.dto.UserUpdateRequest;
import com.eshop.user.exception.UserNotFoundException;
import com.eshop.user.model.User;
import com.eshop.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void createUser_savesAndReturnsResponse() {
        UserRequest req = new UserRequest("John", "Doe", "jdoe", "pass", "j@d.com", LocalDate.of(1990,1,1), "12345", "Male");
        User toSave = User.builder()
                .firstName(req.firstName())
                .lastName(req.lastName())
                .username(req.username())
                .password(req.password())
                .email(req.email())
                .dob(req.dob())
                .phoneNo(req.phoneNo())
                .gender(req.gender())
                .build();

        User saved = User.builder()
                .id(1L)
                .firstName(req.firstName())
                .lastName(req.lastName())
                .username(req.username())
                .password(req.password())
                .email(req.email())
                .dob(req.dob())
                .phoneNo(req.phoneNo())
                .gender(req.gender())
                .build();

        when(userRepository.save(any(User.class))).thenReturn(saved);

        UserResponse resp = userService.createUser(req);

        assertNotNull(resp);
        assertEquals(1L, resp.id());
        assertEquals(req.username(), resp.username());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void updateUser_whenNotFound_throws() {
        when(userRepository.findById(10L)).thenReturn(Optional.empty());

        UserUpdateRequest update = new UserUpdateRequest("A","B","ab@c.com", LocalDate.of(1995,5,5), "99999");

        UserNotFoundException ex = assertThrows(UserNotFoundException.class, () -> userService.updateUser(10L, update));
        assertTrue(ex.getMessage().contains("User not found with id"));
        verify(userRepository, never()).save(any());
    }

    @Test
    void updateUser_updatesAndReturns() {
        User existing = User.builder()
                .id(2L)
                .firstName("Old")
                .lastName("Name")
                .email("old@e.com")
                .phoneNo("111")
                .dob(LocalDate.of(1980,1,1))
                .build();

        UserUpdateRequest update = new UserUpdateRequest("NewFirst", null, "new@e.com", LocalDate.of(1985,2,2), "222");

        when(userRepository.findById(2L)).thenReturn(Optional.of(existing));
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        UserResponse resp = userService.updateUser(2L, update);

        assertNotNull(resp);
        assertEquals(2L, resp.id());
        assertEquals("NewFirst", resp.firstName());
        assertEquals("new@e.com", resp.email());
        assertEquals("222", resp.phoneNo());
        verify(userRepository, times(1)).findById(2L);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void deleteUser_whenNotFound_throws() {
        when(userRepository.existsById(5L)).thenReturn(false);

        UserNotFoundException ex = assertThrows(UserNotFoundException.class, () -> userService.deleteUser(5L));
        assertTrue(ex.getMessage().contains("User not found"));
        verify(userRepository, never()).deleteById(anyLong());
    }

    @Test
    void deleteUser_successDeletes() {
        when(userRepository.existsById(6L)).thenReturn(true);

        userService.deleteUser(6L);

        verify(userRepository, times(1)).deleteById(6L);
    }

    @Test
    void getUserById_whenNotFound_throws() {
        when(userRepository.findById(7L)).thenReturn(Optional.empty());

        UserNotFoundException ex = assertThrows(UserNotFoundException.class, () -> userService.getUserById(7L));
        assertTrue(ex.getMessage().contains("User not found with id"));
    }

    @Test
    void getUserById_returnsResponse() {
        User u = User.builder().id(8L).firstName("F").lastName("L").username("u").password("p").email("e@e.com").dob(LocalDate.of(1970,1,1)).phoneNo("1").gender("M").build();
        when(userRepository.findById(8L)).thenReturn(Optional.of(u));

        UserResponse resp = userService.getUserById(8L);

        assertNotNull(resp);
        assertEquals(8L, resp.id());
        assertEquals(u.getEmail(), resp.email());
    }

    @Test
    void getUsers_whenEmpty_throws() {
        when(userRepository.findAll()).thenReturn(List.of());

        UserNotFoundException ex = assertThrows(UserNotFoundException.class, () -> userService.getUsers());
        assertTrue(ex.getMessage().contains("User not found"));
    }

    @Test
    void getUsers_returnsList() {
        User u1 = User.builder().id(1L).firstName("A").lastName("B").username("a").password("p").email("a@e.com").build();
        User u2 = User.builder().id(2L).firstName("C").lastName("D").username("c").password("p").email("c@e.com").build();
        when(userRepository.findAll()).thenReturn(List.of(u1, u2));

        var responses = userService.getUsers();

        assertNotNull(responses);
        assertEquals(2, responses.size());
        assertEquals(1L, responses.get(0).id());
        verify(userRepository, times(1)).findAll();
    }
}

