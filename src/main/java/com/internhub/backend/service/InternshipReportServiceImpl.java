package com.internhub.backend.service;

import com.internhub.backend.dto.request.internshipreports.CreateInternshipReportRequest;
import com.internhub.backend.dto.request.internshipreports.SubmitInternshipReportRequest;
import com.internhub.backend.dto.request.page.PageSearchSortFilterRequest;
import com.internhub.backend.dto.response.SuccessResponse;
import com.internhub.backend.entity.academic.Enrollment;
import com.internhub.backend.entity.account.Notification;
import com.internhub.backend.entity.account.User;
import com.internhub.backend.entity.student.InternshipReport;
import com.internhub.backend.entity.student.ReportStatus;
import com.internhub.backend.entity.student.Student;
import com.internhub.backend.exception.CustomException;
import com.internhub.backend.exception.EnumException;
import com.internhub.backend.repository.EnrollmentRepository;
import com.internhub.backend.repository.InternshipReportRepository;
import com.internhub.backend.repository.StudentRepository;
import com.internhub.backend.repository.UserRepository;
import com.internhub.backend.util.AuthUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InternshipReportServiceImpl implements InternshipReportService {

    @Value("${admin.email}")
    private String adminEmail;

    private final InternshipReportRepository internshipReportRepository;
    private final StudentRepository studentRepository;
    private final UserRepository userRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final WebSocketService webSocketService;

    @Override
    public void createInternshipReport(CreateInternshipReportRequest request) {
        Authentication authentication = AuthUtils.getAuthenticatedUser();
        Jwt jwt = (Jwt) authentication.getPrincipal();
        String userId = (String) jwt.getClaims().get("userId");

        Student student = studentRepository.findById(userId)
                .orElseThrow(() -> new CustomException(EnumException.PROFILE_NOT_FOUND));

        InternshipReport internshipReport = InternshipReport.builder()
                .student(student)
                .companyName(request.getCompanyName())
                .teacherName(request.getTeacherName())
                .instructorName(request.getInstructorName())
                .instructorEmail(request.getInstructorEmail())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .reportFile(request.getReportFile())
                .evaluationFile(request.getEvaluationFile())
                .createdDate(Date.from(new Date().toInstant()))
                .reportStatus(ReportStatus.PROCESSING)
                .isSystemCompany(request.isSystemCompany())
                .build();

        internshipReportRepository.save(internshipReport);

        User user = userRepository.findByEmail(adminEmail);
        String title = "Có báo cáo thực tập mới đang chờ duyệt";
        Notification notification = Notification.builder()
                .title(title)
                .content("Sinh viên [" + student.getName() + " - " + student.getStudentId() + "] vừa nộp báo cáo thực tập và đang chờ duyệt.")
                .createdDate(Date.from(Instant.now()))
                .user(user)
                .build();
        user.getNotifications().add(notification);
        userRepository.save(user);

        webSocketService.sendPrivateMessage(user.getId(), title);
    }

    @Override
    public SuccessResponse<List<InternshipReport>> getAllInternshipReportsByStudent(PageSearchSortFilterRequest request) {
        Authentication authentication = AuthUtils.getAuthenticatedUser();
        Jwt jwt = (Jwt) authentication.getPrincipal();
        String userId = (String) jwt.getClaims().get("userId");

        Student student = studentRepository.findById(userId)
                .orElseThrow(() -> new CustomException(EnumException.PROFILE_NOT_FOUND));

        Sort sort;
        if ("oldest".equalsIgnoreCase(request.getOrder())) {
            sort = Sort.by(Sort.Order.asc("createdDate"));
        } else {
            sort = Sort.by(Sort.Order.desc("createdDate"));
        }
        Pageable pageable = PageRequest.of(request.getPage() - 1, request.getSize(), sort);
        Page<InternshipReport> pageData = internshipReportRepository.findAllByStudent(student, pageable);

        return SuccessResponse.<List<InternshipReport>>builder()
                .pageInfo(SuccessResponse.PageInfo.builder()
                        .currentPage(request.getPage())
                        .totalPages(pageData.getTotalPages())
                        .pageSize(pageData.getSize())
                        .totalElements(pageData.getTotalElements())
                        .hasPreviousPage(pageData.hasPrevious())
                        .hasNextPage(pageData.hasNext())
                        .build())
                .result(pageData.getContent())
                .build();
    }

    @Override
    public void submitInternshipReport(SubmitInternshipReportRequest request) {
        Authentication authentication = AuthUtils.getAuthenticatedUser();
        Jwt jwt = (Jwt) authentication.getPrincipal();
        String userId = (String) jwt.getClaims().get("userId");

        Student student = studentRepository.findById(userId)
                .orElseThrow(() -> new CustomException(EnumException.PROFILE_NOT_FOUND));

        InternshipReport internshipReport = InternshipReport.builder()
                .student(student)
                .companyName(request.getCompanyName())
                .teacherName(request.getTeacherName())
                .instructorName(request.getInstructorName())
                .instructorEmail(request.getInstructorEmail())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .reportFile(request.getReportFile())
                .evaluationFile(request.getEvaluationFile())
                .createdDate(Date.from(new Date().toInstant()))
                .reportStatus(ReportStatus.PROCESSING)
                .isSystemCompany(request.isSystemCompany())
                .build();

        Enrollment enrollment = enrollmentRepository.findByStudentAndCourse_CourseCode(student, request.getCourseCode()).orElseThrow();
        enrollment.setInternshipReport(internshipReport);
        enrollment.setEnrollmentStatus(Enrollment.EnrollmentStatus.SUBMITTED);

        enrollmentRepository.save(enrollment);
    }
}
