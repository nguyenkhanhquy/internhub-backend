package com.internhub.backend.service;

import com.internhub.backend.dto.business.RecruiterDTO;
import com.internhub.backend.entity.business.Recruiter;
import com.internhub.backend.exception.CustomException;
import com.internhub.backend.exception.EnumException;
import com.internhub.backend.mapper.RecruiterMapper;
import com.internhub.backend.repository.RecruiterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecruiterServiceImpl implements RecruiterService {

    private final RecruiterRepository recruiterRepository;
    private final RecruiterMapper recruiterMapper;

    @Autowired
    public RecruiterServiceImpl(RecruiterRepository recruiterRepository, RecruiterMapper recruiterMapper) {
        this.recruiterRepository = recruiterRepository;
        this.recruiterMapper = recruiterMapper;
    }

    @Override
    public List<RecruiterDTO> getAllRecruiters() {
        List<Recruiter> recruiters = recruiterRepository.findAll();

        return recruiters.stream()
                .map(recruiterMapper::mapRecruiterToRecruiterDTO)
                .toList();
    }

    @Override
    public RecruiterDTO getRecruiterById(String id) {
        Recruiter recruiter = recruiterRepository.findById(id).orElseThrow(() -> new CustomException(EnumException.PROFILE_NOT_FOUND));

        return recruiterMapper.mapRecruiterToRecruiterDTO(recruiter);
    }
}
