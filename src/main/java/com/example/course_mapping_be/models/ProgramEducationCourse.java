package com.example.course_mapping_be.models;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "program_education_course")
public class ProgramEducationCourse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "programEducationId", nullable = false)
    private ProgramEducation programEducation;

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinColumn(name = "courseId", nullable = false)
    private Course course;

    @Column(name = "compulsory")
    private Boolean compulsory = false;

    @Column(name = "numCredits", nullable = false)
    private Integer numCredits;

}
