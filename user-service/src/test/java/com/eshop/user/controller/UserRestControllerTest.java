package com.eshop.user.controller;

import com.eshop.user.dto.UserRequest;
import com.eshop.user.dto.UserResponse;
import com.eshop.user.dto.UserUpdateRequest;
import com.eshop.user.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class UserRestControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    private ObjectMapper objectMapper;

    private UserRequest userRequest;
    private UserResponse userResponse;
    private UserUpdateRequest userUpdateRequest;

    @BeforeEach
    void setUp() {
        // Initialize MockMvc with the controller and mock service
        mockMvc = MockMvcBuilders.standaloneSetup(new UserRestController(userService)).build();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        
        // Setup test data
        userRequest = new UserRequest(
                "John",
                "Smith",
                "john_smith",
                "password123",
                "john@gmail.com",
                LocalDate.of(1990, 5, 15),
                "123456789",
                "Male"
                ,"ADMIN_ROLE"
        );

        userResponse = new UserResponse(
                1L,
                "John",
                "Smith",
                "john_smith",
                "password123",
                "john@gmail.com",
                LocalDate.of(1990, 5, 15),
                "123456789",
                "Male",
                "ADMIN_ROLE"
        );

        userUpdateRequest = new UserUpdateRequest(
                "Jane",
                "Smith",
                "jane@gmail.com",
                LocalDate.of(1992, 3, 20),
                "9999999",
                "",
                "ADMIN_ROLE"
        );
    }

    // ========================= POST Tests =========================

    @Test
    void testCreateUser_WithValidRequest_ReturnsCreated() throws Exception {
        // Arrange
        when(userService.createUser(any(UserRequest.class))).thenReturn(userResponse);

        // Act & Assert
        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", equalTo(1)))
                .andExpect(jsonPath("$.firstName", equalTo("John")))
                .andExpect(jsonPath("$.lastName", equalTo("Smith")))
                .andExpect(jsonPath("$.username", equalTo("john_smith")))
                .andExpect(jsonPath("$.email", equalTo("john@gmail.com")))
                .andExpect(jsonPath("$.phoneNo", equalTo("123456789")))
                .andExpect(jsonPath("$.gender", equalTo("Male")));

        verify(userService, times(1)).createUser(any(UserRequest.class));
    }

    @Test
    void testCreateUser_WithBlankUsername_ReturnsBadRequest() throws Exception {
        // Arrange
         UserRequest invalidRequest = new UserRequest(
                "John",
                "Smith",
                "",  // Blank username
                "password123",
                "john@gmail.com",
                LocalDate.of(1990, 5, 15),
                "123456789",
                "Male",
                 "ADMIN_ROLE"
        );

        // Act & Assert
        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());

        verify(userService, never()).createUser(any(UserRequest.class));
    }

    @Test
    void testCreateUser_WithInvalidEmail_ReturnsBadRequest() throws Exception {
        // Arrange
         UserRequest invalidRequest = new UserRequest(
                "John",
                "Smith",
                "john_smith",
                "password123",
                "invalid-email",  // Invalid email format
                LocalDate.of(1990, 5, 15),
                "123456789",
                "Male",
                 "ADMIN_ROLE"
        );

        // Act & Assert
        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());

        verify(userService, never()).createUser(any(UserRequest.class));
    }

    @Test
    void testCreateUser_WithFutureDate_ReturnsBadRequest() throws Exception {
        // Arrange
         UserRequest invalidRequest = new UserRequest(
                "John",
                "Smith",
                "john_smith",
                "password123",
                "john@gmail.com",
                LocalDate.of(2030, 5, 15),  // Future date
                "123456789",
                "Male",
                 "ADMIN_ROLE"
        );

        // Act & Assert
        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());

        verify(userService, never()).createUser(any(UserRequest.class));
    }

    // ========================= PUT Tests =========================

    @Test
    void testUpdateUser_WithValidRequest_ReturnsOk() throws Exception {
        // Arrange
        Long userId = 1L;
         UserResponse updatedResponse = new UserResponse(
                userId,
                "Jane",
                "Smith",
                "john_smith",
                "password123",
                "jane@gmail.com",
                LocalDate.of(1992, 3, 20),
                "9999999",
                "Female",
                 "ADMIN_ROLE"
        );

        when(userService.updateUser(anyLong(), any(UserUpdateRequest.class))).thenReturn(updatedResponse);

        // Act & Assert
        mockMvc.perform(put("/api/v1/users/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userUpdateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(1)))
                .andExpect(jsonPath("$.firstName", equalTo("Jane")))
                .andExpect(jsonPath("$.lastName", equalTo("Smith")))
                .andExpect(jsonPath("$.email", equalTo("jane@gmail.com")))
                .andExpect(jsonPath("$.phoneNo", equalTo("9999999")));

        verify(userService, times(1)).updateUser(anyLong(), any(UserUpdateRequest.class));
    }

    @Test
    void testUpdateUser_WithInvalidEmail_ReturnsBadRequest() throws Exception {
        // Arrange
        Long userId = 1L;
         UserUpdateRequest invalidRequest = new UserUpdateRequest(
                "Jane",
                "Smith",
                "invalid-email",  // Invalid email
                LocalDate.of(1992, 3, 20),
                "9999999",
                 "",
                 "ADMIN_ROLE"
        );

        // Act & Assert
        mockMvc.perform(put("/api/v1/users/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());

        verify(userService, never()).updateUser(anyLong(), any(UserUpdateRequest.class));
    }

    @Test
    void testUpdateUser_WithFutureDate_ReturnsBadRequest() throws Exception {
        // Arrange
        Long userId = 1L;
         UserUpdateRequest invalidRequest = new UserUpdateRequest(
                "Jane",
                "Smith",
                "jane@gmail.com",
                LocalDate.of(2030, 3, 20),  // Future date
                "9999999",
                 "",
                 "ADMIN_ROLE"
        );

        // Act & Assert
        mockMvc.perform(put("/api/v1/users/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());

        verify(userService, never()).updateUser(anyLong(), any(UserUpdateRequest.class));
    }

    // ========================= DELETE Tests =========================

    @Test
    void testDeleteUser_WithValidId_ReturnsNoContent() throws Exception {
        // Arrange
        Long userId = 1L;
        doNothing().when(userService).deleteUser(anyLong());

        // Act & Assert
        mockMvc.perform(delete("/api/v1/users/{id}", userId))
                .andExpect(status().isNoContent());

        verify(userService, times(1)).deleteUser(userId);
    }

    @Test
    void testDeleteUser_CallsServiceOnce() throws Exception {
        // Arrange
        Long userId = 5L;
        doNothing().when(userService).deleteUser(anyLong());

        // Act & Assert
        mockMvc.perform(delete("/api/v1/users/{id}", userId))
                .andExpect(status().isNoContent());

        verify(userService, times(1)).deleteUser(5L);
    }

    // ========================= GET Single User Tests =========================

    @Test
    void testGetUser_WithValidId_ReturnsOk() throws Exception {
        // Arrange
        Long userId = 1L;
        when(userService.getUserById(anyLong())).thenReturn(userResponse);

        // Act & Assert
        mockMvc.perform(get("/api/v1/users/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(1)))
                .andExpect(jsonPath("$.firstName", equalTo("John")))
                .andExpect(jsonPath("$.lastName", equalTo("Smith")))
                .andExpect(jsonPath("$.username", equalTo("john_smith")))
                .andExpect(jsonPath("$.email", equalTo("john@gmail.com")))
                .andExpect(jsonPath("$.gender", equalTo("Male")));

        verify(userService, times(1)).getUserById(userId);
    }

    @Test
    void testGetUser_WithValidId_VerifiesServiceCall() throws Exception {
        // Arrange
        Long userId = 10L;
        when(userService.getUserById(anyLong())).thenReturn(userResponse);

        // Act & Assert
        mockMvc.perform(get("/api/v1/users/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(userService, times(1)).getUserById(10L);
    }

    // ========================= GET All Users Tests =========================

    @Test
    void testGetAllUsers_ReturnsOkWithList() throws Exception {
        // Arrange
         UserResponse user1 = new UserResponse(
                1L,
                "John",
                "Smith",
                "john_smith",
                "password123",
                "john@gmail.com",
                LocalDate.of(1990, 5, 15),
                "123456789",
                "Male",
                 "ADMIN_ROLE"
        );

         UserResponse user2 = new UserResponse(
                2L,
                "Jane",
                "Doe",
                "jane_doe",
                "password456",
                "jane@gmail.com",
                LocalDate.of(1992, 3, 20),
                "9999999",
                "Female",
                 "ADMIN_ROLE"
        );

        List<UserResponse> userList = Arrays.asList(user1, user2);
        when(userService.getUsers()).thenReturn(userList);

        // Act & Assert
        mockMvc.perform(get("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", equalTo(1)))
                .andExpect(jsonPath("$[0].firstName", equalTo("John")))
                .andExpect(jsonPath("$[0].email", equalTo("john@gmail.com")))
                .andExpect(jsonPath("$[1].id", equalTo(2)))
                .andExpect(jsonPath("$[1].firstName", equalTo("Jane")))
                .andExpect(jsonPath("$[1].email", equalTo("jane@gmail.com")));

        verify(userService, times(1)).getUsers();
    }

    @Test
    void testGetAllUsers_WithEmptyList_ReturnsOkWithEmptyArray() throws Exception {
        // Arrange
        when(userService.getUsers()).thenReturn(Arrays.asList());

        // Act & Assert
        mockMvc.perform(get("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));

        verify(userService, times(1)).getUsers();
    }

    @Test
    void testGetAllUsers_WithMultipleUsers_ReturnsCorrectCount() throws Exception {
        // Arrange
        List<UserResponse> userList = Arrays.asList(
                userResponse,
                new UserResponse(
                        2L,
                        "Jane",
                        "Doe",
                        "jane_doe",
                        "password456",
                        "jane@gmail.com",
                        LocalDate.of(1992, 3, 20),
                        "9999999",
                        "Female",
                        "ADMIN_ROLE"
                ),
                new UserResponse(
                        3L,
                        "Bob",
                        "Johnson",
                        "bob_johnson",
                        "password789",
                        "bob@gmail.com",
                        LocalDate.of(1988, 7, 10),
                        "555555555",
                        "Male",
                        "ADMIN_ROLE"
                )
        );

        when(userService.getUsers()).thenReturn(userList);

        // Act & Assert
        mockMvc.perform(get("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].username", equalTo("john_smith")))
                .andExpect(jsonPath("$[1].username", equalTo("jane_doe")))
                .andExpect(jsonPath("$[2].username", equalTo("bob_johnson")));

        verify(userService, times(1)).getUsers();
    }

    // ========================= Content Type Tests =========================

    @Test
    void testCreateUser_WithoutContentType_ReturnsBadRequest() throws Exception {
        // Act & Assert
        mockMvc.perform(post("/api/v1/users")
                        .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isUnsupportedMediaType());

        verify(userService, never()).createUser(any(UserRequest.class));
    }

    @Test
    void testCreateUser_ReturnsApplicationJsonContentType() throws Exception {
        // Arrange
        when(userService.createUser(any(UserRequest.class))).thenReturn(userResponse);

        // Act & Assert
        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(userService, times(1)).createUser(any(UserRequest.class));
    }

    @Test
    void testGetAllUsers_ReturnsApplicationJsonContentType() throws Exception {
        // Arrange
        when(userService.getUsers()).thenReturn(Arrays.asList(userResponse));

        // Act & Assert
        mockMvc.perform(get("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(userService, times(1)).getUsers();
    }
}

