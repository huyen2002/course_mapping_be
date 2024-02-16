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

    @ManyToOne
    @JoinColumn(name = "program_education_id")
    private ProgramEducation program_education;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    @Column(name = "compulsory")
    private Boolean compulsory = false;

    @Column(name = "num_credits", nullable = false)
    private Integer num_credits;

}
