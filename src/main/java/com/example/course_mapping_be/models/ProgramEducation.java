package com.example.course_mapping_be.models;

import com.example.course_mapping_be.constraints.LevelEducationType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


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

    @Column(name = "duration_year", nullable = false)
    private Float duration_year;


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

    @Column(name = "source_links", columnDefinition = "TEXT")
    private String source_links;

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinColumn(name = "university_id", nullable = false)
    private University university;

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "major_id", nullable = false)
    private Major major;

    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "program_education", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<ProgramEducationCourse> programEducationCourses;

}
