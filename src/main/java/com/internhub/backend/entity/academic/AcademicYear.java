package com.internhub.backend.entity.academic;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "academic_year")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class AcademicYear {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private String id;

    @Column(name = "name", unique = true, updatable = false, nullable = false)
    private String name;
}
