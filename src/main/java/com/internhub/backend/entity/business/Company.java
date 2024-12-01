package com.internhub.backend.entity.business;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "company")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private String id;

    @Column(name = "name", unique = true, nullable = false)
    private String name;

    @Column(name = "website")
    private String website;

    @Column(name = "description", length = 3000)
    private String description;

    @Column(name = "address")
    private String address;

    @Column(name = "logo")
    private String logo;

    @OneToOne(mappedBy = "company", cascade = CascadeType.ALL)
    @ToString.Exclude
    @JsonIgnore
    private Recruiter recruiter;
}
