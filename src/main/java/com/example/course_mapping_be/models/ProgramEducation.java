package com.example.course_mapping_be.models;

import com.example.course_mapping_be.constraints.LevelEducationType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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

    @Column(name = "code", nullable = false)
    private String code;

    @Column(name = "language", nullable = false)
    private String language;

    @Column(name = "introduction", columnDefinition = "TEXT")
    private String introduction;

    @Column(name = "durationYear", nullable = false)
    private Float durationYear;


    @Enumerated(EnumType.STRING)
    @Column(name = "levelOfEducation", nullable = false)
    private LevelEducationType levelOfEducation;

    @Column(name = "numCredits", nullable = false)
    private Integer numCredits;

    @Column(name = "outline")
    private String outline;

    @Column(name = "startYear")
    private Integer startYear;

    @Column(name = "endYear")
    private Integer endYear;

    @Column(name = "sourceLinks", columnDefinition = "TEXT")
    private String sourceLinks;

    @Column(name = "vectorDocument", columnDefinition = "TEXT")
    private String vectorDocument;

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinColumn(name = "universityId", nullable = false)
    private University university;

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "majorId", nullable = false)
    private Major major;

    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "programEducation", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<ProgramEducationCourse> programEducationCourses;

}
