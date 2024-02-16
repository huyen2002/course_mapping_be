package com.example.course_mapping_be.models;

import com.example.course_mapping_be.constraints.LevelEducationType;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;


@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Entity
@Builder
@Table(name = "program_educations")
public class ProgramEducation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "language", nullable = false)
    private String language;

    @Column(name = "introduction")
    private String introduction;

    @Column(name = "duration_year")
    private Integer duration_year;


    @Enumerated(EnumType.STRING)
    @Column(name = "level_of_education", nullable = false)
    private LevelEducationType level_of_education;

    @Column(name = "num_credits", nullable = false)
    private Integer num_credits;

    @Column(name = "outline")
    private String outline;

    @Column(name = "start_year", nullable = false)
    private Integer start_year;

    @Column(name = "end_year", nullable = false)
    private Integer end_year;

    @ManyToOne
    @JoinColumn(name = "university_id", nullable = false)
    private University university;

    @ManyToOne
    @JoinColumn(name = "major_id", nullable = false)
    private Major major;

    @OneToMany(mappedBy = "program_education")
    private List<ProgramEducationCourse> programEducationCourses;

}
