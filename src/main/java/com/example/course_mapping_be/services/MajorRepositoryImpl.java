package com.example.course_mapping_be.services;

import com.example.course_mapping_be.dtos.SearchMajorDto;
import com.example.course_mapping_be.models.Major;
import com.example.course_mapping_be.repositories.CustomMajorRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MajorRepositoryImpl implements CustomMajorRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<Major> searchMajors(SearchMajorDto searchMajorDto, Pageable pageable) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Major> criteriaQuery = criteriaBuilder.createQuery(Major.class);
        Root<Major> root = criteriaQuery.from(Major.class);
        List<Predicate> predicates = new ArrayList<>();
        if (searchMajorDto.getName() != null) {
            predicates.add(criteriaBuilder.like(root.get("name"), "%" + searchMajorDto.getName() + "%"));
        }
        if (searchMajorDto.getCode() != null) {
            predicates.add(criteriaBuilder.equal(root.get("code"), searchMajorDto.getCode()));
        }
        if (searchMajorDto.getEnabled() == null || searchMajorDto.getEnabled()) {
            predicates.add(criteriaBuilder.isTrue(root.get("enabled")));
        } else {
            predicates.add(criteriaBuilder.isFalse(root.get("enabled")));
        }
        criteriaQuery.where(predicates.toArray(new Predicate[0]));
        Long total = getTotalSearchMajors(searchMajorDto, pageable);
        List<Major> majors = entityManager.createQuery(criteriaQuery)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();
        return new PageImpl<>(majors, pageable, total);
    }

    private Long getTotalSearchMajors(SearchMajorDto searchMajorDto, Pageable pageable) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
        Root<Major> root = criteriaQuery.from(Major.class);
        List<Predicate> predicates = new ArrayList<>();
        if (searchMajorDto.getName() != null) {
            predicates.add(criteriaBuilder.like(root.get("name"), "%" + searchMajorDto.getName() + "%"));
        }
        if (searchMajorDto.getCode() != null) {
            predicates.add(criteriaBuilder.equal(root.get("code"), searchMajorDto.getCode()));
        }
        if (searchMajorDto.getEnabled() == null || searchMajorDto.getEnabled()) {
            predicates.add(criteriaBuilder.isTrue(root.get("enabled")));
        } else {
            predicates.add(criteriaBuilder.isFalse(root.get("enabled")));
        }
        criteriaQuery.where(predicates.toArray(new Predicate[0]));
        criteriaQuery.select(criteriaBuilder.count(root));
        return entityManager.createQuery(criteriaQuery).getSingleResult();
    }
}
