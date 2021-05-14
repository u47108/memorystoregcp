package com.latam.bebigquery.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.latam.bebigquery.entity.Student;

@Repository
public interface StudentRepository extends PagingAndSortingRepository<Student, String> {
}
