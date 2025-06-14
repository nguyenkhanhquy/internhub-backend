package com.internhub.backend.service;

import com.internhub.backend.dto.admin.OverviewDTO;
import com.internhub.backend.dto.job.jobpost.JobPostDetailDTO;
import com.internhub.backend.dto.request.jobs.DeleteJobPostRequest;
import com.internhub.backend.dto.request.jobs.JobPostSearchFilterRequest;
import com.internhub.backend.dto.request.page.PageSearchSortFilterRequest;
import com.internhub.backend.dto.response.SuccessResponse;
import com.internhub.backend.entity.academic.Semester;
import com.internhub.backend.entity.account.Notification;
import com.internhub.backend.entity.account.User;
import com.internhub.backend.entity.business.Recruiter;
import com.internhub.backend.entity.job.JobPost;
import com.internhub.backend.entity.student.InternStatus;
import com.internhub.backend.entity.student.InternshipReport;
import com.internhub.backend.entity.student.ReportStatus;
import com.internhub.backend.entity.student.Student;
import com.internhub.backend.entity.teacher.Teacher;
import com.internhub.backend.exception.CustomException;
import com.internhub.backend.exception.EnumException;
import com.internhub.backend.mapper.JobPostMapper;
import com.internhub.backend.repository.*;
import com.internhub.backend.util.AcademicUtils;
import com.internhub.backend.util.AuthUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final CourseRepository courseRepository;
    private final JobPostRepository jobPostRepository;
    private final InternshipReportRepository internshipReportRepository;
    private final JobPostMapper jobPostMapper;
    private final RecruiterRepository recruiterRepository;
    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;
    private final UserRepository userRepository;
    private final AcademicYearRepository academicYearRepository;
    private final WebSocketService webSocketService;

    @Override
    public OverviewDTO getOverview() {
        Authentication authentication = AuthUtils.getAuthenticatedUser();
        Jwt jwt = (Jwt) authentication.getPrincipal();
        String userId = (String) jwt.getClaims().get("userId");
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(EnumException.USER_NOT_FOUND));

        long totalNotifications = user.getNotificationCount();
        long totalNotificationsNotRead = user.getNotificationNotReadCount();
        long totalCourses = courseRepository.count();
        long totalStudents = studentRepository.count();
        long totalStudentsNotReported = studentRepository.countStudentByIsReported(false);
        long totalTeachers = teacherRepository.count();
        long totalRecruiters = recruiterRepository.count();
        long totalRecruitersNotApproved = recruiterRepository.countRecruiterByIsApproved(false);
        long totalJobPosts = jobPostRepository.count();
        long totalJobPostsDisplayed = jobPostRepository.countJobPostByIsApprovedAndIsDeleted(false, false);

        long totalStudentsSearching = studentRepository.countByInternStatus(InternStatus.SEARCHING);
        long totalStudentsWorking = studentRepository.countByInternStatus(InternStatus.WORKING);
        long totalStudentsCompleted = studentRepository.countByInternStatus(InternStatus.COMPLETED);

        return OverviewDTO.builder()
                .totalNotifications(totalNotifications)
                .totalNotificationsNotRead(totalNotificationsNotRead)
                .totalCourses(totalCourses)
                .totalStudents(totalStudents)
                .totalStudentsNotReported(totalStudentsNotReported)
                .totalTeachers(totalTeachers)
                .totalRecruiters(totalRecruiters)
                .totalRecruitersNotApproved(totalRecruitersNotApproved)
                .totalJobPosts(totalJobPosts)
                .totalJobPostsNotApproved(totalJobPostsDisplayed)
                .totalStudentsSearching(totalStudentsSearching)
                .totalStudentsWorking(totalStudentsWorking)
                .totalStudentsCompleted(totalStudentsCompleted)
                .build();
    }

    @Override
    public SuccessResponse<List<InternshipReport>> getAllInternshipReports(PageSearchSortFilterRequest request) {
        Sort sort = Sort.by(Sort.Order.desc("createdDate"));
        Pageable pageable = PageRequest.of(request.getPage() - 1, request.getSize(), sort);
        Page<InternshipReport> pageData = internshipReportRepository.adminFindAllInternshipReports(request.getSearch(), pageable);

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
    public void approveInternshipReport(String id) {
        InternshipReport internshipReport = internshipReportRepository.findById(id)
                .orElseThrow(() -> new CustomException(EnumException.INTERNSHIP_REPORT_NOT_FOUND));
        internshipReport.setReportStatus(ReportStatus.ACCEPTED);
        internshipReportRepository.save(internshipReport);

        User user = internshipReport.getStudent().getUser();
        String title = "Báo cáo thực tập của bạn đã được duyệt";
        Notification notification = Notification.builder()
                .title(title)
                .content("Chúc mừng! Báo cáo thực tập của bạn đã được duyệt")
                .createdDate(Date.from(Instant.now()))
                .user(user)
                .build();
        user.getNotifications().add(notification);
        userRepository.save(user);

        Student student = studentRepository.findById(user.getId())
                .orElseThrow(() -> new CustomException(EnumException.PROFILE_NOT_FOUND));
        student.setReported(true);
        student.setInternStatus(InternStatus.COMPLETED);
        studentRepository.save(student);

        webSocketService.sendPrivateMessage(user.getId(), title);
    }

    @Override
    public void rejectInternshipReport(String id) {
        InternshipReport internshipReport = internshipReportRepository.findById(id)
                .orElseThrow(() -> new CustomException(EnumException.INTERNSHIP_REPORT_NOT_FOUND));
        internshipReport.setReportStatus(ReportStatus.REJECTED);
        internshipReportRepository.save(internshipReport);

        User user = internshipReport.getStudent().getUser();
        String title = "Báo cáo thực tập của bạn đã bị từ chối";
        Notification notification = Notification.builder()
                .title(title)
                .content("Báo cáo thực tập của bạn đã bị từ chối, vui lòng kiểm tra lại thông tin và gửi lại báo cáo")
                .createdDate(Date.from(Instant.now()))
                .user(user)
                .build();
        user.getNotifications().add(notification);
        userRepository.save(user);

        webSocketService.sendPrivateMessage(user.getId(), title);
    }

    @Override
    public SuccessResponse<List<JobPostDetailDTO>> getAllJobPosts(JobPostSearchFilterRequest request) {
        Sort sort = Sort.by(Sort.Order.desc("updatedDate"));
        Pageable pageable = PageRequest.of(request.getPage() - 1, request.getSize(), sort);
        Page<JobPost> pageData = jobPostRepository.adminFindAllJobPosts(request.getSearch(), pageable);

        return SuccessResponse.<List<JobPostDetailDTO>>builder()
                .pageInfo(SuccessResponse.PageInfo.builder()
                        .currentPage(request.getPage())
                        .totalPages(pageData.getTotalPages())
                        .pageSize(pageData.getSize())
                        .totalElements(pageData.getTotalElements())
                        .hasPreviousPage(pageData.hasPrevious())
                        .hasNextPage(pageData.hasNext())
                        .build())
                .result(pageData.getContent().stream()
                        .map(jobPostMapper::mapJobPostToJobPostDetailDTO)
                        .toList())
                .build();
    }

    @Override
    public void approveJobPost(String id) {
        JobPost jobPost = jobPostRepository.findById(id)
                .orElseThrow(() -> new CustomException(EnumException.JOB_POST_NOT_FOUND));
        jobPost.setApproved(true);
        jobPostRepository.save(jobPost);

        Recruiter recruiter = jobPost.getCompany().getRecruiter();
        if (recruiter == null) {
            throw new CustomException(EnumException.PROFILE_NOT_FOUND);
        }

        User user = recruiter.getUser();
        String title = "Bài đăng: " + jobPost.getTitle() + " đã được duyệt";
        Notification notification = Notification.builder()
                .title(title)
                .content("Bài đăng: " + jobPost.getTitle() + " đã được duyệt và có thể được hiển thị")
                .createdDate(Date.from(Instant.now()))
                .user(user)
                .build();
        user.getNotifications().add(notification);
        userRepository.save(user);

        webSocketService.sendPrivateMessage(user.getId(), title);
    }

    @Override
    public void deleteJobPost(String id, DeleteJobPostRequest request) {
        JobPost jobPost = jobPostRepository.findById(id)
                .orElseThrow(() -> new CustomException(EnumException.JOB_POST_NOT_FOUND));
        jobPost.setDeleted(true);
        jobPostRepository.save(jobPost);

        Recruiter recruiter = jobPost.getCompany().getRecruiter();
        if (recruiter == null) {
            throw new CustomException(EnumException.PROFILE_NOT_FOUND);
        }

        User user = recruiter.getUser();
        String title = "Bài đăng: " + jobPost.getTitle() + " đã bị từ chối";
        Notification notification = Notification.builder()
                .title(title)
                .content("Bài đăng: " + jobPost.getTitle() + " đã bị từ chối với lý do [ " + request.getReason() + " ] và không thể hiển thị")
                .createdDate(Date.from(Instant.now()))
                .user(user)
                .build();
        user.getNotifications().add(notification);
        userRepository.save(user);

        webSocketService.sendPrivateMessage(user.getId(), title);
    }

    @Override
    public SuccessResponse<List<Recruiter>> getAllRecruiters(PageSearchSortFilterRequest request) {
        Sort sort = Sort.by(Sort.Order.desc("user.updatedDate"));
        Pageable pageable = PageRequest.of(request.getPage() - 1, request.getSize(), sort);
        Page<Recruiter> pageData = recruiterRepository.findAllRecruiters(request.getSearch(), pageable);

        return SuccessResponse.<List<Recruiter>>builder()
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
    public void approveRecruiter(String id) {
        Recruiter recruiter = recruiterRepository.findById(id)
                .orElseThrow(() -> new CustomException(EnumException.PROFILE_NOT_FOUND));
        recruiter.setApproved(true);
        recruiterRepository.save(recruiter);

        User user = recruiter.getUser();
        String title = "Hồ sơ doanh nghiệp của bạn đã được duyệt";
        Notification notification = Notification.builder()
                .title(title)
                .content("Hồ sơ doanh nghiệp của bạn đã xem xét và đã được chấp nhận, vui lòng tải lại trang để sử dụng các chức năng tuyển dụng")
                .createdDate(Date.from(Instant.now()))
                .user(user)
                .build();
        user.getNotifications().add(notification);
        userRepository.save(user);

        webSocketService.sendPrivateMessage(user.getId(), title);
    }

    @Override
    public SuccessResponse<List<Student>> getAllStudents(PageSearchSortFilterRequest request) {
        Sort sort = Sort.by(Sort.Order.desc("user.updatedDate"));
        Pageable pageable = PageRequest.of(request.getPage() - 1, request.getSize(), sort);
        Page<Student> pageData = studentRepository.adminFindAllStudents(request.getSearch(), pageable);

        return SuccessResponse.<List<Student>>builder()
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
    public SuccessResponse<List<Teacher>> getAllTeachers(PageSearchSortFilterRequest request) {
        Sort sort = Sort.by(Sort.Order.desc("user.updatedDate"));
        Pageable pageable = PageRequest.of(request.getPage() - 1, request.getSize(), sort);
        Page<Teacher> pageData = teacherRepository.adminFindAllTeachers(request.getSearch(), pageable);

        return SuccessResponse.<List<Teacher>>builder()
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
    public List<Student> getAllStudentsNotEnrolledInSemester() {
        LocalDate now = LocalDate.now(ZoneId.of("Asia/Ho_Chi_Minh"));
        String currentAcademicYear = AcademicUtils.getCurrentAcademicYear(now, academicYearRepository).getName();
        Semester currentSemester = AcademicUtils.getCurrentSemester(now);
        return studentRepository.findStudentsNotEnrolledInSemester(currentAcademicYear, currentSemester);
    }
}
