package com.internhub.backend.entity.teacher;

import com.internhub.backend.entity.account.User;
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
    private String userId;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "user_id")
    @MapsId
    private User user;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "teacher_id", nullable = false, unique = true)
    private String teacherId;
}
