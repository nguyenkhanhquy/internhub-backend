package com.internhub.backend.service;

import com.internhub.backend.dto.academic.EnrollmentDTO;
import com.internhub.backend.entity.academic.Course;
import com.internhub.backend.entity.academic.Enrollment;
import com.internhub.backend.entity.student.ReportStatus;
import com.internhub.backend.entity.teacher.Teacher;
import com.internhub.backend.exception.CustomException;
import com.internhub.backend.exception.EnumException;
import com.internhub.backend.mapper.EnrollmentMapper;
import com.internhub.backend.repository.EnrollmentRepository;
import com.internhub.backend.repository.TeacherRepository;
import com.internhub.backend.util.AuthUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EnrollmentServiceImpl implements EnrollmentService {

    private final NotificationService notificationService;
    private final EnrollmentRepository enrollmentRepository;
    private final TeacherRepository teacherRepository;
    private final EnrollmentMapper enrollmentMapper;

    @Override
    public Page<EnrollmentDTO> getAllEnrollmentsByStudent(Pageable pageable, String search) {
        Authentication authentication = AuthUtils.getAuthenticatedUser();
        Jwt jwt = (Jwt) authentication.getPrincipal();
        String userId = (String) jwt.getClaims().get("userId");

        if (search == null || search.isBlank()) {
            return enrollmentRepository.findByStudent_UserIdAndCourse_CourseStatusNot(userId, Course.CourseStatus.DRAFT, pageable)
                    .map(enrollmentMapper::toDTO);
        }

        return enrollmentRepository.findByStudent_UserIdAndCourse_CourseCodeContainingIgnoreCaseAndCourse_CourseStatusNot(userId, search, Course.CourseStatus.DRAFT, pageable)
                .map(enrollmentMapper::toDTO);
    }

    @Override
    public void updateFinalScore(String enrollmentId, Double finalScore, String feedback) {
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new CustomException(EnumException.ENROLLMENT_NOT_FOUND));

        Authentication authentication = AuthUtils.getAuthenticatedUser();
        Jwt jwt = (Jwt) authentication.getPrincipal();
        String userId = (String) jwt.getClaims().get("userId");

        Teacher teacher = teacherRepository.findById(userId)
                .orElseThrow(() -> new CustomException(EnumException.PROFILE_NOT_FOUND));

        // Kiểm tra xem giảng viên có quyền nhập điểm cho khóa học này không
        if (!enrollmentRepository.existsByIdAndCourse_Teacher(enrollmentId, teacher)) {
            throw new CustomException(EnumException.UNAUTHORIZED);
        }

        // Cập nhật điểm và phản hồi
        enrollment.setFinalScore(finalScore);
        enrollment.setFeedback(feedback);
        if (finalScore < 5) {
            enrollment.setEnrollmentStatus(Enrollment.EnrollmentStatus.FAILED);
        } else {
            enrollment.getStudent().setReported(true);
            enrollment.setEnrollmentStatus(Enrollment.EnrollmentStatus.COMPLETED);
        }
        enrollment.getInternshipReport().setReportStatus(ReportStatus.ACCEPTED);

        enrollmentRepository.save(enrollment);

        // Gửi thông báo cho sinh viên
        String title = "[" + enrollment.getCourse().getCourseCode() + "] Đã có điểm và phản hồi từ giảng viên";
        String content = "Giảng viên " + teacher.getName() + " đã nhập điểm và phản hồi cho bạn trong khóa học [" +
                enrollment.getCourse().getCourseCode() + "]";
        notificationService.sendNotification(enrollment.getStudent().getUser(), title, content);
    }
}
