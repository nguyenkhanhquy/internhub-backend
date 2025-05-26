package com.internhub.backend.service;

import com.internhub.backend.dto.cv.CurriculumVitaeDTO;
import com.internhub.backend.dto.request.cv.CreateCVRequest;
import com.internhub.backend.entity.student.CurriculumVitae;
import com.internhub.backend.entity.student.Student;
import com.internhub.backend.exception.CustomException;
import com.internhub.backend.exception.EnumException;
import com.internhub.backend.mapper.CurriculumVitaeMapper;
import com.internhub.backend.repository.CurriculumVitaeRepository;
import com.internhub.backend.repository.StudentRepository;
import com.internhub.backend.util.AuthUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
@RequiredArgsConstructor
public class CurriculumVitaeServiceImpl implements CurriculumVitaeService {

    private final CurriculumVitaeRepository curriculumVitaeRepository;
    private final StudentRepository studentRepository;
    private final CurriculumVitaeMapper curriculumVitaeMapper;

    @Override
    public void createCurriculumVitae(CreateCVRequest request) {
        Authentication authentication = AuthUtils.getAuthenticatedUser();
        Jwt jwt = (Jwt) authentication.getPrincipal();
        String userId = (String) jwt.getClaims().get("userId");

        Student student = studentRepository.findById(userId)
                .orElseThrow(() -> new CustomException(EnumException.PROFILE_NOT_FOUND));

        CurriculumVitae cv = CurriculumVitae.builder()
                .title(request.getTitle())
                .filePath(request.getFilePath())
                .createdDate(LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")))
                .student(student)
                .build();

        curriculumVitaeRepository.save(cv);
    }

    @Override
    public Page<CurriculumVitaeDTO> getAllCVsByStudent(Pageable pageable, String search) {
        Authentication authentication = AuthUtils.getAuthenticatedUser();
        Jwt jwt = (Jwt) authentication.getPrincipal();
        String userId = (String) jwt.getClaims().get("userId");

        if (search == null || search.isBlank()) {
            return curriculumVitaeRepository.findByStudent_UserId(pageable, userId)
                    .map(curriculumVitaeMapper::toDTO);
        }

        return curriculumVitaeRepository.findByStudent_UserIdAndTitleContainingIgnoreCase(pageable, userId, search)
                .map(curriculumVitaeMapper::toDTO);
    }

    @Override
    public void deleteCurriculumVitae(String cvId) {
        // Kiểm tra xem CV có tồn tại không
        CurriculumVitae cv = curriculumVitaeRepository.findById(cvId)
                .orElseThrow(() -> new CustomException(EnumException.CV_NOT_FOUND));

        // Kiểm tra xem CV có thuộc về người dùng hiện tại không
        Authentication authentication = AuthUtils.getAuthenticatedUser();
        Jwt jwt = (Jwt) authentication.getPrincipal();
        String userId = (String) jwt.getClaims().get("userId");
        if (!cv.getStudent().getUserId().equals(userId)) {
            throw new CustomException(EnumException.UNAUTHORIZED);
        }

        // Xóa CV
        curriculumVitaeRepository.delete(cv);
    }
}
