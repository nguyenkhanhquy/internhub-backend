package com.internhub.backend.entity.business;

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

    @Lob
    @Column(name = "description")
    private String description;

    @Column(name = "address")
    private String address;

    @Column(name = "logo")
    private String logo;
}
