package com.example.course_mapping_be.services;

import com.example.course_mapping_be.dtos.FilterProgramParams;
import com.example.course_mapping_be.models.ProgramEducation;
import com.example.course_mapping_be.repositories.CustomProgramEducationRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class ProgramEducationRepositoryImpl implements CustomProgramEducationRepository {
    @PersistenceContext
    private EntityManager entityManager;

    private final String FILTER_ALL = "ALL";

    @Override
    public List<ProgramEducation> findAllByFilterParams(FilterProgramParams filterProgramParams) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<ProgramEducation> criteriaQuery = criteriaBuilder.createQuery(ProgramEducation.class);
        Root<ProgramEducation> root = criteriaQuery.from(ProgramEducation.class);
        List<Predicate> predicates = new ArrayList<>();

        String country = filterProgramParams.getCountry();
        if (Objects.nonNull(country) && !country.equals(FILTER_ALL)) {
            if (country.startsWith("!")) {
                predicates.add(criteriaBuilder.notEqual(root.get("university").get("address").get("country"), country.substring(1)));
            } else {
                predicates.add(criteriaBuilder.equal(root.get("university").get("address").get("country"), country));
            }
        }
        String language = filterProgramParams.getLanguage();
        if (Objects.nonNull(language) && !language.equals(FILTER_ALL)) {
            if (language.startsWith("!")) {
                predicates.add(criteriaBuilder.notEqual(root.get("language"), language.substring(1)));
            } else {
                predicates.add(criteriaBuilder.equal(root.get("language"), language));
            }
        }
        String major = filterProgramParams.getMajor();
        if (Objects.nonNull(major) && !major.equals(FILTER_ALL)) {
            if (major.startsWith("!")) {
                predicates.add(criteriaBuilder.notEqual(root.get("major"), major.substring(1)));
            } else {
                predicates.add(criteriaBuilder.equal(root.get("major"), major));
            }
        }

        criteriaQuery.where(predicates.toArray(new Predicate[0]));
        return entityManager.createQuery(criteriaQuery).getResultList();
    }
}
