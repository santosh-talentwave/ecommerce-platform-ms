package com.eshop.user.service;

import com.eshop.user.dto.UserRequest;
import com.eshop.user.dto.UserResponse;
import com.eshop.user.dto.UserUpdateRequest;
import com.eshop.user.exception.UserNotFoundException;
import com.eshop.user.mapper.UserMapper;
import com.eshop.user.model.User;
import com.eshop.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

//    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;

    @Override
    public UserResponse createUser(UserRequest request) {
        log.info("Creating user: {}", request);
        User user = UserMapper.toEntity(request);
        User savedUser = userRepository.save(user);
        log.info("Saved user: {}", savedUser);
        return UserMapper.toResponse(savedUser);
    }

    @Override
    public UserResponse updateUser(Long id, UserUpdateRequest request) {
        log.info("Updating user: {}", request);
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty()) {
            log.error("User with id {} not found", id);
            throw new UserNotFoundException("User not found with id " + id);
        }
        User user = UserMapper.toEntity(optionalUser.get(), request);
        User updatedUser = userRepository.save(user);
        log.info("Updated user: {}", updatedUser);
        return UserMapper.toResponse(updatedUser);
    }

    @Override
    public void deleteUser(long id) {
        log.info("Deleting user: {}", id);
        boolean userExists = userRepository.existsById(id);
        if (!userExists) {
            log.error("User with id {} not found", id);
            throw new UserNotFoundException("User not found with id " + id);
        }
        userRepository.deleteById(id);
        log.info("Deleted user: {}", id);
    }

    @Override
    public UserResponse getUserById(long id) {
        log.info("Retrieving user by id: {}", id);
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty()) {
            log.error("User with id {} not found", id);
            throw new UserNotFoundException("User not found with id " + id);
        }
        log.info("Retrieved user: {}", optionalUser.get());
        return optionalUser.map(UserMapper::toResponse).orElse(null);
    }

    @Override
    public List<UserResponse> getUsers() {
        log.info("Retrieving all users");
        List<User> users = userRepository.findAll();
        if (users.isEmpty()) {
            log.error("User list is empty");
            throw new UserNotFoundException("User not found");
        }
        log.info("Retrieved users: {}", users);
        return users.stream().map(UserMapper::toResponse).collect(Collectors.toList());
    }
}
