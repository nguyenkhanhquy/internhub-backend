package com.internhub.backend.service;

import com.internhub.backend.dto.request.user.CreateUserRequest;
import com.internhub.backend.dto.user.UserDTO;
import com.internhub.backend.entity.User;
import com.internhub.backend.exception.CustomException;
import com.internhub.backend.exception.EnumException;
import com.internhub.backend.mapper.UserMapper;
import com.internhub.backend.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public List<UserDTO> getAllUsers() {

        List<User> users = userRepository.findAll();

        return users.stream()
                .map(userMapper::mapUserToUserDTO)
                .toList();
    }

    @Override
    public UserDTO getUserById(String id) {

        User user = userRepository.findById(id).orElseThrow(() -> new CustomException(EnumException.USER_NOT_FOUND));

        return userMapper.mapUserToUserDTO(user);
    }

    @Override
    public UserDTO createUser(CreateUserRequest createUserRequest) {

        try {
            User user = User.builder()
                    .email(createUserRequest.getEmail())
                    .password(createUserRequest.getPassword())
//                    .password(passwordEncoder.encode(createUserRequest.getPassword()))
                    .isActive(true)
                    .registrationDate(Date.from(Instant.now()))
//                    .role(roleRepository.findByName("STUDENT")
                    .build();

            User savedUser = userRepository.save(user);

            return userMapper.mapUserToUserDTO(savedUser);
        } catch (DataIntegrityViolationException e) {
            throw new CustomException(EnumException.USER_EXISTED);
        }
    }
}
