package com.example.course_mapping_be.services;

import com.example.course_mapping_be.dtos.FilterUniversityParams;
import com.example.course_mapping_be.models.University;
import com.example.course_mapping_be.repositories.CustomUniversityRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class UniversityRepositoryImpl implements CustomUniversityRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<University> filterUniversities(FilterUniversityParams filterParams, Pageable pageable) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<University> criteriaQuery = criteriaBuilder.createQuery(University.class);
        Root<University> root = criteriaQuery.from(University.class);

        Predicate predicate = criteriaBuilder.conjunction();

        if (filterParams.getName() != null) {
            predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(root.get("name"), "%" + filterParams.getName() + "%"));
        }

        String country = filterParams.getCountry();
        if (Objects.nonNull(country) && !country.equals("ALL")) {
            if (country.startsWith("!")) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.notEqual(root.get("address").get("country"), country.substring(1)));
            } else {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("address").get("country"), country));
            }
        }
        if (filterParams.getEnabled() != null) {
            predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("enabled"), filterParams.getEnabled()));
        } else {
            predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("enabled"), true));
        }

        criteriaQuery.where(predicate);

        TypedQuery<University> query = entityManager.createQuery(criteriaQuery);
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        List<University> universities = query.getResultList();

        Long total = getCountFilterUniversities(filterParams);

        return new PageImpl<>(universities, pageable, total);

    }

    private Long getCountFilterUniversities(FilterUniversityParams filterParams) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
        Root<University> root = countQuery.from(University.class);

        Predicate predicate = criteriaBuilder.conjunction();

        if (filterParams.getName() != null) {
            predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(root.get("name"), "%" + filterParams.getName() + "%"));
        }

        String country = filterParams.getCountry();
        if (Objects.nonNull(country) && !country.equals("ALL")) {
            if (country.startsWith("!")) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.notEqual(root.get("address").get("country"), country.substring(1)));
            } else {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("address").get("country"), country));
            }
        }

        countQuery.select(criteriaBuilder.count(root));
        countQuery.where(predicate);

        return entityManager.createQuery(countQuery).getSingleResult();
    }
}
