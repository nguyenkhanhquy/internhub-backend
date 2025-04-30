package com.internhub.backend.entity.academic;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonValue;
import com.internhub.backend.entity.teacher.Teacher;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "course")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private String id;

    @Column(name = "course_code", nullable = false, unique = true)
    private String courseCode;

    @Column(name = "course_name", nullable = false)
    private String courseName;

    @ManyToOne
    @JoinColumn(name = "academic_year_name", referencedColumnName = "name", nullable = false)
    private AcademicYear academicYear;

    @Enumerated(EnumType.STRING)
    @Column(name = "semester")
    private Semester semester;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @ManyToOne
    @JoinColumn(name = "teacher_user_id", referencedColumnName = "user_id", nullable = false)
    private Teacher teacher;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @Builder.Default
    @JsonManagedReference
    private List<Enrollment> enrollments = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    @Builder.Default
    private CourseStatus courseStatus = CourseStatus.GRADING;

    public enum CourseStatus {
        GRADING("Đang nhập điểm"),
        FINALIZED("Đã kết thúc");

        private final String label;

        CourseStatus(String label) {
            this.label = label;
        }

        @JsonValue
        public String getLabel() {
            return label;
        }

        @JsonCreator
        public static CourseStatus fromLabel(String label) {
            for (CourseStatus status : CourseStatus.values()) {
                if (status.label.equalsIgnoreCase(label)) {
                    return status;
                }
            }
            throw new IllegalArgumentException("Nhãn trạng thái không hợp lệ: " + label);
        }
    }

    public int getTotalStudents() {
        return enrollments != null ? enrollments.size() : 0;
    }
}
