package com.example.course_mapping_be.models;

import com.example.course_mapping_be.constraints.DataStatus;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.beans.factory.annotation.Value;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "comparable_program_education")
public class ComparableProgramEducation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;


    @Column(name = "firstProgramId", nullable = false)
    private Long firstProgramId;

    
    @Column(name = "secondProgramId", nullable = false)
    private Long secondProgramId;

    @Column(name = "coursesMapping", columnDefinition = "LONGTEXT")
    private String coursesMapping;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    @Value("UPDATED")
    private DataStatus status;
}
