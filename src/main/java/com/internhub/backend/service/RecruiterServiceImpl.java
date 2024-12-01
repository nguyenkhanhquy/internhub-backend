package com.internhub.backend.service;

import com.internhub.backend.dto.business.RecruiterDTO;
import com.internhub.backend.dto.request.recruiters.UpdateRecruiterProfileRequest;
import com.internhub.backend.entity.account.Notification;
import com.internhub.backend.entity.account.User;
import com.internhub.backend.entity.business.Company;
import com.internhub.backend.entity.business.Recruiter;
import com.internhub.backend.exception.CustomException;
import com.internhub.backend.exception.EnumException;
import com.internhub.backend.mapper.RecruiterMapper;
import com.internhub.backend.repository.RecruiterRepository;
import com.internhub.backend.repository.UserRepository;
import com.internhub.backend.util.AuthUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RecruiterServiceImpl implements RecruiterService {

    private final RecruiterRepository recruiterRepository;
    private final RecruiterMapper recruiterMapper;
    private final UserRepository userRepository;
    private final WebSocketService webSocketService;

    @Override
    public List<RecruiterDTO> getAllRecruiters() {
        List<Recruiter> recruiters = recruiterRepository.findAll();

        return recruiters.stream()
                .map(recruiterMapper::toDTO)
                .toList();
    }

    @Override
    public RecruiterDTO getRecruiterById(String id) {
        Recruiter recruiter = recruiterRepository.findById(id).orElseThrow(() -> new CustomException(EnumException.PROFILE_NOT_FOUND));

        return recruiterMapper.toDTO(recruiter);
    }

    @Override
    public void updateRecruiterProfile(UpdateRecruiterProfileRequest request) {
        Authentication authentication = AuthUtils.getAuthenticatedUser();
        Jwt jwt = (Jwt) authentication.getPrincipal();
        String userId = (String) jwt.getClaims().get("userId");

        Recruiter recruiter = recruiterRepository.findById(userId)
                .orElseThrow(() -> new CustomException(EnumException.PROFILE_NOT_FOUND));

        Company company = recruiter.getCompany();
        company.setWebsite(request.getWebsite());
        company.setDescription(request.getDescription());
        company.setAddress(request.getCompanyAddress());
        company.setLogo(request.getCompanyLogo());

        recruiter.setCompany(company);
        recruiter.setName(request.getName());
        recruiter.setPosition(request.getPosition());
        recruiter.setPhone(request.getPhone());
        recruiter.setRecruiterEmail(request.getRecruiterEmail());

        recruiterRepository.save(recruiter);
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
}
