package com.internhub.backend.service;

import com.internhub.backend.dto.TextMessageDTO;
import com.internhub.backend.dto.job.jobpost.JobPostDetailDTO;
import com.internhub.backend.dto.request.jobs.JobPostSearchFilterRequest;
import com.internhub.backend.dto.response.SuccessResponse;
import com.internhub.backend.entity.account.Notification;
import com.internhub.backend.entity.account.User;
import com.internhub.backend.entity.business.Recruiter;
import com.internhub.backend.entity.job.JobPost;
import com.internhub.backend.exception.CustomException;
import com.internhub.backend.exception.EnumException;
import com.internhub.backend.mapper.JobPostMapper;
import com.internhub.backend.repository.JobPostRepository;
import com.internhub.backend.repository.RecruiterRepository;
import com.internhub.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final SimpMessagingTemplate template;
    private final JobPostRepository jobPostRepository;
    private final JobPostMapper jobPostMapper;
    private final RecruiterRepository recruiterRepository;
    private final UserRepository userRepository;

    @Override
    public SuccessResponse<List<JobPostDetailDTO>> getAllJobPosts(JobPostSearchFilterRequest request) {
        Sort sort = Sort.by(Sort.Order.desc("updatedDate"));

        Pageable pageable = PageRequest.of(request.getPage() - 1, request.getSize(), sort);

        Page<JobPost> pageData = jobPostRepository.findAll(pageable);

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

        Recruiter recruiter = recruiterRepository.findByCompany(jobPost.getCompany());
        if (recruiter == null) {
            throw new CustomException(EnumException.PROFILE_NOT_FOUND);
        }
        User user = recruiter.getUser();
        Notification notification = Notification.builder()
                .title("Bài đăng " + jobPost.getTitle() + " đã được duyệt")
                .content("Bài đăng " + jobPost.getTitle() + " đã được duyệt")
                .createdDate(Date.from(Instant.now()))
                .user(user)
                .build();
        user.getNotifications().add(notification);
        userRepository.save(user);

        TextMessageDTO textMessageDTO = new TextMessageDTO();
        textMessageDTO.setMessage("Một bài đăng tuyển dụng đã được duyệt");
        template.convertAndSendToUser(
                user.getId(),
                "/private",
                textMessageDTO
        );
    }
}
