package com.internhub.backend.service;

import com.internhub.backend.dto.account.UserDTO;
import com.internhub.backend.dto.request.users.*;
import jakarta.mail.MessagingException;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface UserService {

    @PreAuthorize("hasAuthority('SCOPE_FIT')")
    List<UserDTO> getAllUsers();

    @PostAuthorize("returnObject.email == authentication.name or hasAuthority('SCOPE_FIT')")
    UserDTO getUserById(String id);

    @PreAuthorize("hasAuthority('SCOPE_FIT')")
    UserDTO createUser(CreateUserRequest createUserRequest);

    @PostAuthorize("returnObject.email == authentication.name or hasAuthority('SCOPE_FIT')")
    UserDTO updateUser(String id, UpdateUserRequest updateUserRequest);

    @PreAuthorize("hasAuthority('SCOPE_FIT')")
    UserDTO deleteUser(String id);

    UserDTO registerRecruiter(RegisterRecruiterRequest registerRecruiterRequest);

    UserDTO registerStudent(RegisterStudentRequest registerStudentRequest);

    @PreAuthorize("hasAuthority('SCOPE_FIT')")
    void importTeachersFromFile(MultipartFile file);

    void sendOTP(Map<String, String> request) throws MessagingException;

    void activateAccount(Map<String, String> request);

    void requestActivateAccount(Map<String, String> request);

    void resetPassword(Map<String, String> request);

    void updatePassword(UpdatePasswordRequest updatePasswordRequest);
}
