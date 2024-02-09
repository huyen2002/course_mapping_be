package com.example.course_mapping_be.repositories;

import com.example.course_mapping_be.models.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {
}
