package com.internhub.backend.entity.teacher;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "teacher")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Teacher {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private String id;

    @Column(name = "name", nullable = false)
    private String name;
}
