package com.internhub.backend.dto.admin;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OverviewDTO {

    private long totalNotifications;
    private long totalNotificationsNotRead;

    private long totalCourses;

    private long totalStudents;
    private long totalStudentsNotReported;

    private long totalTeachers;

    private long totalRecruiters;
    private long totalRecruitersNotApproved;

    private long totalJobPosts;
    private long totalJobPostsNotApproved;

    private long totalStudentsSearching;
    private long totalStudentsWorking;
    private long totalStudentsCompleted;
}
