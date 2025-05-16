package com.internhub.backend.entity.academic;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.internhub.backend.entity.student.InternshipReport;
import com.internhub.backend.entity.student.Student;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "enrollment", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"course_id", "student_id"})
})
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Enrollment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private String id;

    @ManyToOne
    @JoinColumn(name = "course_id", referencedColumnName = "id", nullable = false)
    @JsonBackReference
    private Course course;

    @ManyToOne
    @JoinColumn(name = "student_id", referencedColumnName = "student_id", nullable = false)
    private Student student;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "report_id", referencedColumnName = "id")
    private InternshipReport internshipReport;

    @Column(name = "final_score")
    private Double finalScore;

    @Column(name = "feedback")
    private String feedback;

    @CreatedDate
    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Column(name = "updated_date", nullable = false)
    private LocalDateTime updatedDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    @Builder.Default
    private EnrollmentStatus enrollmentStatus = EnrollmentStatus.NOT_SUBMITTED;

    public enum EnrollmentStatus {
        NOT_SUBMITTED("Chưa nộp báo cáo"),
        SUBMITTED("Đã nộp báo cáo"),
        COMPLETED("Hoàn thành"),
        FAILED("Không đạt");

        private final String label;

        EnrollmentStatus(String label) {
            this.label = label;
        }

        @JsonValue
        public String getLabel() {
            return label;
        }

        @JsonCreator
        public static EnrollmentStatus fromLabel(String label) {
            for (EnrollmentStatus status : EnrollmentStatus.values()) {
                if (status.label.equalsIgnoreCase(label)) {
                    return status;
                }
            }
            throw new IllegalArgumentException("Trạng thái ghi danh không hợp lệ: " + label);
        }
    }
}
